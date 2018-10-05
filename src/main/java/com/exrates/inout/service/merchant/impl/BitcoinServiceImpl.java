package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.btc.CoreWalletService;
import com.exrates.inout.service.merchant.BitcoinService;
import com.exrates.inout.util.BigDecimalProcessing;
import com.exrates.inout.util.ParamMapUtils;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Log4j2(topic = "bitcoin_core")
public class BitcoinServiceImpl implements BitcoinService {

  @Value("${btcInvoice.blockNotifyUsers}")
  private Boolean BLOCK_NOTIFYING;

  @Autowired
  private RefillService refillService;
  @Autowired
  private CurrencyService currencyService;
  @Autowired
  private MerchantService merchantService;

  @Autowired
  private MerchantSpecParamsDao merchantSpecParamsDao;

  @Autowired
  private MessageSource messageSource;
  @Autowired
  private CoreWalletService bitcoinWalletService;

  private String backupFolder;

  private String nodePropertySource;

  private Boolean zmqEnabled;
  
  private String merchantName;
  
  private String currencyName;
  
  private Integer minConfirmations;

  private Integer blockTargetForFee;

  private Boolean rawTxEnabled;

  private Boolean supportInstantSend;

  private Boolean nodeEnabled;

  private Boolean supportSubtractFee;

  private Boolean supportWalletNotifications;

  private Boolean supportReferenceLine;

  private ScheduledExecutorService newTxCheckerScheduler = Executors.newSingleThreadScheduledExecutor();



  @Override
  public Integer minConfirmationsRefill() {
    return minConfirmations;
  }

  public BitcoinServiceImpl(String propertySource, String merchantName, String currencyName, Integer minConfirmations, Integer blockTargetForFee, Boolean rawTxEnabled) {
    this(propertySource, merchantName, currencyName, minConfirmations, blockTargetForFee, rawTxEnabled, true);
  }

  public BitcoinServiceImpl(String propertySource, String merchantName, String currencyName, Integer minConfirmations, Integer blockTargetForFee,
                            Boolean rawTxEnabled, Boolean supportSubtractFee) {
    this(propertySource, merchantName, currencyName, minConfirmations, blockTargetForFee, rawTxEnabled, supportSubtractFee, true);
  }

  public BitcoinServiceImpl(String propertySource, String merchantName, String currencyName, Integer minConfirmations, Integer blockTargetForFee,
                            Boolean rawTxEnabled, Boolean supportSubtractFee, Boolean supportWalletNotifications) {
    this(propertySource, merchantName, currencyName, minConfirmations, blockTargetForFee, rawTxEnabled, supportSubtractFee, supportWalletNotifications, false);
  }

  public BitcoinServiceImpl(String propertySource, String merchantName, String currencyName, Integer minConfirmations, Integer blockTargetForFee,
                            Boolean rawTxEnabled, Boolean supportSubtractFee, Boolean supportWalletNotifications, Boolean supportReferenceLine) {
    Properties props = new Properties();
    try {
      props.load(getClass().getClassLoader().getResourceAsStream(propertySource));
      this.backupFolder = props.getProperty("backup.folder");
      this.nodePropertySource = props.getProperty("node.propertySource");
      this.nodeEnabled = Boolean.valueOf(props.getProperty("node.isEnabled"));
      this.zmqEnabled = Boolean.valueOf(props.getProperty("node.zmqEnabled"));
      this.supportInstantSend = Boolean.valueOf(props.getProperty("node.supportInstantSend"));
      this.merchantName = merchantName;
      this.currencyName = currencyName;
      this.minConfirmations = minConfirmations;
      this.blockTargetForFee = blockTargetForFee;
      this.rawTxEnabled = rawTxEnabled;
      this.supportSubtractFee = supportSubtractFee;
      this.supportWalletNotifications = supportWalletNotifications;
      this.supportReferenceLine = supportReferenceLine;
    } catch (IOException e) {
      log.error(e);
    }
  }

  @Override
  public boolean isRawTxEnabled() {
    return rawTxEnabled;
  }

  public BitcoinServiceImpl(Boolean zmqEnabled, String merchantName, String currencyName, Integer minConfirmations) {
    this.zmqEnabled = zmqEnabled;
    this.merchantName = merchantName;
    this.currencyName = currencyName;
    this.minConfirmations = minConfirmations;
  }



  @PostConstruct
  void startBitcoin() {
    if (nodeEnabled) {
      bitcoinWalletService.initCoreClient(nodePropertySource, supportInstantSend, supportSubtractFee, supportReferenceLine);
      bitcoinWalletService.initBtcdDaemon(zmqEnabled);
      bitcoinWalletService.blockFlux().subscribe(this::onIncomingBlock);
      if (supportWalletNotifications) {
        bitcoinWalletService.walletFlux().subscribe(this::onPayment);
      } else {
        newTxCheckerScheduler.scheduleAtFixedRate(this::checkForNewTransactions, 3, 1, TimeUnit.MINUTES);
      }
      if (supportInstantSend) {
        bitcoinWalletService.instantSendFlux().subscribe(this::onPayment);
      }
//      CompletableFuture.runAsync(this::examineMissingPaymentsOnStartup);
      examineMissingPaymentsOnStartup();
    }

  }


  @Override
  @Transactional
  public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
    BigDecimal withdrawAmount = new BigDecimal(withdrawMerchantOperationDto.getAmount());
    String txId = bitcoinWalletService.sendToAddressAuto(withdrawMerchantOperationDto.getAccountTo(), withdrawAmount, getCoreWalletPassword());
    return Collections.singletonMap("hash", txId);
  }

  private String getCoreWalletPassword() {
    return merchantService.getCoreWalletPassword(merchantName, currencyName)
            .orElseThrow(() -> new CoreWalletPasswordNotFoundException(String.format("pass not found for merchant %s currency %s", merchantName, currencyName)));

  }

  @Override
  @Transactional
  public Map<String, String> refill(RefillRequestCreateDto request) {
    String address = address();
    String message = messageSource.getMessage("merchants.refill.btc",
        new Object[]{address}, request.getLocale());
    return new HashMap<String, String>() {{
      put("address", address);
      put("message", message);
      put("qr", address);
    }};
  }

  @Override
  public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
    Currency currency = currencyService.findByName(currencyName);
    Merchant merchant = merchantService.findByName(merchantName);
    String address = ParamMapUtils.getIfNotNull(params, "address");
    String txId = ParamMapUtils.getIfNotNull(params, "txId");
    BtcTransactionDto btcTransactionDto = bitcoinWalletService.getTransaction(txId);
    Integer confirmations = btcTransactionDto.getConfirmations();
    BigDecimal amount = btcTransactionDto.getDetails().stream().filter(payment -> address.equals(payment.getAddress()))
            .findFirst().orElseThrow(BtcPaymentNotFoundException::new).getAmount();
    processBtcPayment(BtcPaymentFlatDto.builder()
            .amount(amount)
            .confirmations(confirmations)
            .txId(txId)
            .address(address)
            .merchantId(merchant.getId())
            .currencyId(currency.getId()).build());
  }

  
  private String address() {
    boolean isFreshAddress = false;
    String address = bitcoinWalletService.getNewAddress(getCoreWalletPassword());
    Currency currency = currencyService.findByName(currencyName);
    Merchant merchant = merchantService.findByName(merchantName);
//    if (refillService.existsUnclosedRefillRequestForAddress(address, merchant.getId(), currency.getId())) {
//      final int LIMIT = 2000;
//      int i = 0;
//      while (!isFreshAddress && i++ < LIMIT) {
//        address = bitcoinWalletService.getNewAddress(walletPassword);
//        isFreshAddress = !refillService.existsUnclosedRefillRequestForAddress(address, merchant.getId(), currency.getId());
//      }
//      if (i >= LIMIT) {
//        throw new IllegalStateException("Can`t generate fresh address");
//      }
//    }
    return address;
  }

  @Override
  public void onPayment(BtcTransactionDto transactionDto) {
    log.info("on payment {} - {}", currencyName, transactionDto);

    try {
      Merchant merchant = merchantService.findByName(merchantName);
      Currency currency = currencyService.findByName(currencyName);
      Optional<BtcTransactionDto> targetTxResult = bitcoinWalletService.handleTransactionConflicts(transactionDto.getTxId());
      if (targetTxResult.isPresent()) {
        BtcTransactionDto targetTx = targetTxResult.get();
        targetTx.getDetails().stream().filter(payment -> "RECEIVE".equalsIgnoreCase( payment.getCategory()))
                .forEach(payment -> {
                  log.debug("Payment " + payment);
                  BtcPaymentFlatDto btcPaymentFlatDto = BtcPaymentFlatDto.builder()
                          .txId(targetTx.getTxId())
                          .address(payment.getAddress())
                          .amount(payment.getAmount())
                          .confirmations(targetTx.getConfirmations())
                          .blockhash(targetTx.getBlockhash())
                          .merchantId(merchant.getId())
                          .currencyId(currency.getId()).build();
                  try {
                    processBtcPayment(btcPaymentFlatDto);
                  } catch (Exception e) {
                    log.error(e);
                  }
                });
      } else {
        log.error("Invalid transaction");
      }
    } catch (Exception e) {
      log.error(e);
    }
  }
  
  void processBtcPayment(BtcPaymentFlatDto btcPaymentFlatDto) {
    if (!checkTransactionAlreadyOnBchExam(btcPaymentFlatDto.getAddress(), btcPaymentFlatDto.getMerchantId(),
            btcPaymentFlatDto.getCurrencyId(), btcPaymentFlatDto.getTxId())) {
      Optional<Integer> refillRequestIdResult = refillService.getRequestIdInPendingByAddressAndMerchantIdAndCurrencyId(
              btcPaymentFlatDto.getAddress(), btcPaymentFlatDto.getMerchantId(), btcPaymentFlatDto.getCurrencyId());
      Integer requestId = refillRequestIdResult.orElseGet(() ->
              refillService.createRefillRequestByFact(RefillRequestAcceptDto.builder()
                      .address(btcPaymentFlatDto.getAddress())
                      .amount(btcPaymentFlatDto.getAmount())
                      .merchantId(btcPaymentFlatDto.getMerchantId())
                      .currencyId(btcPaymentFlatDto.getCurrencyId())
                      .merchantTransactionId(btcPaymentFlatDto.getTxId()).build()));
      if (btcPaymentFlatDto.getConfirmations() >= 0 && btcPaymentFlatDto.getConfirmations() < minConfirmations) {
        try {
          log.info("put on bch exam {}", btcPaymentFlatDto );
          refillService.putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto.builder()
                  .requestId(requestId)
                  .merchantId(btcPaymentFlatDto.getMerchantId())
                  .currencyId(btcPaymentFlatDto.getCurrencyId())
                  .address( btcPaymentFlatDto.getAddress())
                  .amount(btcPaymentFlatDto.getAmount())
                  .hash(btcPaymentFlatDto.getTxId())
                  .blockhash(btcPaymentFlatDto.getBlockhash()).build());
        } catch (RefillRequestAppropriateNotFoundException e) {
          log.error(e);
        }
      } else {
        changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                .requestId(requestId)
                .address(btcPaymentFlatDto.getAddress())
                .amount(btcPaymentFlatDto.getAmount())
                .confirmations(btcPaymentFlatDto.getConfirmations())
                .currencyId(btcPaymentFlatDto.getCurrencyId())
                .merchantId(btcPaymentFlatDto.getMerchantId())
                .hash(btcPaymentFlatDto.getTxId())
                .blockhash(btcPaymentFlatDto.getBlockhash()).build());
      }
    }
  }
  
  private boolean checkTransactionAlreadyOnBchExam( String address,
                                                    Integer merchantId,
                                                    Integer currencyId,
                                                    String hash) {
    return refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(address, merchantId, currencyId, hash).isPresent();
  }
  
  @Override
  public void onIncomingBlock(BtcBlockDto blockDto) {
    String blockHash = blockDto.getHash();
    log.info("incoming block {} - {}", currencyName, blockHash);
    try {
      Merchant merchant = merchantService.findByName(merchantName);
      Currency currency = currencyService.findByName(currencyName);
      List<RefillRequestFlatDto> btcRefillRequests = refillService.getInExamineByMerchantIdAndCurrencyIdList(merchant.getId(), currency.getId());
      log.info("Refill requests ready for update: " +
        btcRefillRequests.stream().map(RefillRequestFlatDto::getId).collect(Collectors.toList()));

      List<RefillRequestSetConfirmationsNumberDto> paymentsToUpdate = new ArrayList<>();
      btcRefillRequests.stream().filter(request -> StringUtils.isNotEmpty(request.getMerchantTransactionId())).forEach(request -> {
        try {
          Optional<BtcTransactionDto> txResult = bitcoinWalletService.handleTransactionConflicts(request.getMerchantTransactionId());
          if (txResult.isPresent()) {
            BtcTransactionDto tx = txResult.get();
            tx.getDetails().stream().filter(paymentOverview -> request.getAddress().equals(paymentOverview.getAddress()))
                    .findFirst().ifPresent(paymentOverview -> {
                      paymentsToUpdate.add(RefillRequestSetConfirmationsNumberDto.builder()
                              .address(paymentOverview.getAddress())
                              .amount(paymentOverview.getAmount())
                              .currencyId(currency.getId())
                              .merchantId(merchant.getId())
                              .requestId(request.getId())
                              .confirmations(tx.getConfirmations())
                              .blockhash(blockHash)
                              .hash(tx.getTxId()).build());
                    }
                    );

          } else {
            log.warn("No valid transactions available!");
          }
        } catch (Exception e) {
          log.error(e);
        }

      });

      log.info("updating payments: " + paymentsToUpdate);
      paymentsToUpdate.forEach(payment -> {
        log.debug(String.format("Payment to update: %s", payment));
        changeConfirmationsOrProvide(payment);
      });
    } catch (Exception e) {
      log.error(e);
    }


  }
  
  
  void changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto dto) {
    try {
      refillService.setConfirmationCollectedNumber(dto);
      if (dto.getConfirmations() >= minConfirmations) {
        log.info("Providing transaction {}", dto.getHash());
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .requestId(dto.getRequestId())
                .address(dto.getAddress())
                .amount(dto.getAmount())
                .currencyId(dto.getCurrencyId())
                .merchantId(dto.getMerchantId())
                .merchantTransactionId(dto.getHash())
                .build();
        refillService.autoAcceptRefillRequest(requestAcceptDto);
      }
    } catch (Exception e) {
      log.error(e);
    }
    
  }
  
  @Override
  @Scheduled(initialDelay = 5 * 60000, fixedDelay = 12 * 60 * 60000)
  public void backupWallet() {
    bitcoinWalletService.backupWallet(backupFolder);
  }
  
  @Override
  public BtcWalletInfoDto getWalletInfo() {
    return bitcoinWalletService.getWalletInfo();
  }
  
  @Override
  public List<BtcTransactionHistoryDto> listAllTransactions() {
    return bitcoinWalletService.listAllTransactions();
  }
  
  @Override
  public BigDecimal estimateFee() {
    return bitcoinWalletService.estimateFee(40);
  }

  @Override
  public String getEstimatedFeeString() {
    BigDecimal feeRate = estimateFee();
    if (feeRate.compareTo(BigDecimal.ZERO) < 0) {
      return "N/A";
    }
    return BigDecimalProcessing.formatNonePoint(feeRate, true);
  }
  
  @Override
  public BigDecimal getActualFee() {
    return bitcoinWalletService.getActualFee();
  }
  
  @Override
  public void setTxFee(BigDecimal fee) {
    bitcoinWalletService.setTxFee(fee);
  }
  
  @Override
  public void submitWalletPassword(String password) {
    String storedPassword = getCoreWalletPassword();
    if (password == null || !password.equals(storedPassword)) {
      throw new IncorrectCoreWalletPasswordException("Incorrect password: " + password);
    }
    bitcoinWalletService.submitWalletPassword(password);
  }
  
  @Override
  public List<BtcPaymentResultDetailedDto> sendToMany(List<BtcWalletPaymentItemDto> payments) {
    List<Map<String, BigDecimal>> paymentGroups = groupPaymentsForSeparateTransactions(payments);
    Currency currency = currencyService.findByName(currencyName);
    Merchant merchant = merchantService.findByName(merchantName);
    boolean subtractFeeFromAmount = merchantService.getSubtractFeeFromAmount(merchant.getId(), currency.getId());

    return paymentGroups.stream()
            .flatMap(group -> {
              BtcPaymentResultDto result = bitcoinWalletService.sendToMany(group, subtractFeeFromAmount);
              return group.entrySet().stream().map(entry -> new BtcPaymentResultDetailedDto(entry.getKey(),
                      entry.getValue(), result));
                    }).collect(Collectors.toList());
  }

  private List<Map<String, BigDecimal>> groupPaymentsForSeparateTransactions(List<BtcWalletPaymentItemDto> payments) {
    List<Map<String, BigDecimal>> paymentGroups = new ArrayList<>();
    paymentGroups.add(new LinkedHashMap<>());
    for (BtcWalletPaymentItemDto payment : payments) {

      ListIterator<Map<String, BigDecimal>> paymentGroupIterator = paymentGroups.listIterator();
      boolean isProcessed = false;
      while (paymentGroupIterator.hasNext() && !isProcessed) {
        Map<String, BigDecimal> group = paymentGroupIterator.next();
        if (!group.containsKey(payment.getAddress())) {
          group.put(payment.getAddress(), payment.getAmount());
          isProcessed = true;
        }
      }
      if (!isProcessed) {
        Map<String, BigDecimal> newPaymentGroup = new LinkedHashMap<>();
        newPaymentGroup.put(payment.getAddress(), payment.getAmount());
        paymentGroupIterator.add(newPaymentGroup);
      }
    }
    return paymentGroups;
  }

  @Override
  public BtcAdminPreparedTxDto prepareRawTransactions(List<BtcWalletPaymentItemDto> payments) {
    List<Map<String, BigDecimal>> paymentGroups = groupPaymentsForSeparateTransactions(payments);
    BigDecimal feeRate = getActualFee();

    return new BtcAdminPreparedTxDto(paymentGroups.stream().map(group -> bitcoinWalletService.prepareRawTransaction(group))
            .collect(Collectors.toList()), feeRate) ;
  }

  @Override
  public BtcAdminPreparedTxDto updateRawTransactions(List<BtcPreparedTransactionDto> preparedTransactions) {
    BigDecimal feeRate = getActualFee();
    return new BtcAdminPreparedTxDto(preparedTransactions.stream()
            .map(transactionDto -> bitcoinWalletService.prepareRawTransaction(transactionDto.getPayments(), transactionDto.getHex()))
            .collect(Collectors.toList()), feeRate) ;
  }

  @Override
  public List<BtcPaymentResultDetailedDto> sendRawTransactions(List<BtcPreparedTransactionDto> preparedTransactions) {
    return preparedTransactions.stream().flatMap(preparedTx -> {
      BtcPaymentResultDto resultDto = bitcoinWalletService.signAndSendRawTransaction(preparedTx.getHex());
      return preparedTx.getPayments().entrySet().stream()
              .map(payment -> new BtcPaymentResultDetailedDto(payment.getKey(), payment.getValue(), resultDto));
    }).collect(Collectors.toList());
  }

  private void examineMissingPaymentsOnStartup() {
    Merchant merchant = merchantService.findByName(merchantName);
    Currency currency = currencyService.findByName(currencyName);
    refillService.getLastBlockHashForMerchantAndCurrency(merchant.getId(), currency.getId()).ifPresent(lastKnownBlockHash -> {
      bitcoinWalletService.listSinceBlock(lastKnownBlockHash, merchant.getId(), currency.getId()).forEach(btcPaymentFlatDto -> {
        try {
          processBtcPayment(btcPaymentFlatDto);
        } catch (Exception e) {
          log.error(e);
        }
      });
    });
  }

  @Override
  public void scanForUnprocessedTransactions(@Nullable String blockHash) {
    Merchant merchant = merchantService.findByName(merchantName);
    Currency currency = currencyService.findByName(currencyName);
    bitcoinWalletService.listSinceBlockEx(blockHash, merchant.getId(), currency.getId()).forEach(btcPaymentFlatDto -> {
      try {
        processBtcPayment(btcPaymentFlatDto);
      } catch (Exception e) {
        log.error(e);
      }
    });
    try {
      onIncomingBlock(bitcoinWalletService.getBlockByHash(bitcoinWalletService.getLastBlockHash()));
    } catch (Exception e) {
      log.error(e);
    }

  }

  private void checkForNewTransactions() {
    log.info("Start checking new {} transactions: ", currencyName);
    try {
      Merchant merchant = merchantService.findByName(merchantName);
      Currency currency = currencyService.findByName(currencyName);
      String blockParamName = "lastBlock";
      MerchantSpecParamDto lastBlockParam = merchantSpecParamsDao.getByMerchantIdAndParamName(merchant.getId(), blockParamName);
      if (lastBlockParam == null) {
        throw new MerchantSpecParamNotFoundException(String.format("merchant %s, currency %s, param %s", merchant.getName(), currency.getName(),
                blockParamName));
      }
      String currentBlockHash = bitcoinWalletService.getLastBlockHash();
      List<BtcPaymentFlatDto> payments = bitcoinWalletService.listSinceBlock(lastBlockParam.getParamValue(), merchant.getId(), currency.getId());
      payments.forEach(btcPaymentFlatDto -> {
        try {
          log.info("Processing tx {}", btcPaymentFlatDto);
          processBtcPayment(btcPaymentFlatDto);
        } catch (Exception e) {
          log.error(e);
        }
      });
      merchantSpecParamsDao.updateParam(merchantName, blockParamName, currentBlockHash);
    } catch (Exception e) {
      log.error(e);
    }

  }



  @Override
  public String getNewAddressForAdmin() {
    return bitcoinWalletService.getNewAddress(getCoreWalletPassword());
  }

  @Override
  public void setSubtractFeeFromAmount(boolean subtractFeeFromAmount) {
    Currency currency = currencyService.findByName(currencyName);
    Merchant merchant = merchantService.findByName(merchantName);
    merchantService.setSubtractFeeFromAmount(merchant.getId(), currency.getId(), subtractFeeFromAmount);

  }

  @Override
  public boolean getSubtractFeeFromAmount() {
    Currency currency = currencyService.findByName(currencyName);
    Merchant merchant = merchantService.findByName(merchantName);
    return merchantService.getSubtractFeeFromAmount(merchant.getId(), currency.getId());
  }

  @PreDestroy
  public void shutdown() {
    bitcoinWalletService.shutdown();
    newTxCheckerScheduler.shutdown();
  }

}

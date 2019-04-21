package com.exrates.inout.service.btcCore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.domain.dto.BtcBlockDto;
import com.exrates.inout.domain.dto.BtcPaymentFlatDto;
import com.exrates.inout.domain.dto.BtcPaymentResultDto;
import com.exrates.inout.domain.dto.BtcPreparedTransactionDto;
import com.exrates.inout.domain.dto.BtcTransactionDto;
import com.exrates.inout.domain.dto.BtcTransactionHistoryDto;
import com.exrates.inout.domain.dto.BtcTxOutputDto;
import com.exrates.inout.domain.dto.BtcTxPaymentDto;
import com.exrates.inout.domain.dto.BtcWalletInfoDto;
import com.exrates.inout.domain.dto.TxReceivedByAddressFlatDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.exceptions.BitcoinCoreException;
import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.InvalidAccountException;
import com.exrates.inout.exceptions.MerchantException;
import com.exrates.inout.properties.models.BitcoinNode;
import com.exrates.inout.service.btcCore.btcDaemon.BtcDaemon;
import com.exrates.inout.service.btcCore.btcDaemon.BtcHttpDaemonImpl;
import com.exrates.inout.service.btcCore.btcDaemon.BtcdZMQDaemonImpl;
import com.exrates.inout.util.BigDecimalProcessing;
import com.google.common.collect.ImmutableList;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.core.domain.Address;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.FundingResult;
import com.neemre.btcdcli4j.core.domain.OutputOverview;
import com.neemre.btcdcli4j.core.domain.RawTransactionOverview;
import com.neemre.btcdcli4j.core.domain.SignatureResult;
import com.neemre.btcdcli4j.core.domain.SinceBlock;
import com.neemre.btcdcli4j.core.domain.SmartFee;
import com.neemre.btcdcli4j.core.domain.Transaction;
import com.neemre.btcdcli4j.core.domain.WalletInfo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zeromq.ZMQ;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

/**
 * Created by OLEG on 14.03.2017.
 */
@Component
@Scope("prototype")
//@Log4j2(topic = "bitcoin_core")
public class CoreWalletServiceImpl implements CoreWalletService {

   private static final Logger log = LogManager.getLogger("bitcoin_core");

  
  private static final int KEY_POOL_LOW_THRESHOLD = 10;
  private static final int MIN_CONFIRMATIONS_FOR_SPENDING = 3;
  private static final int TRANSACTIONS_PER_PAGE_FOR_SEARCH = 500;

  @Autowired
  private ZMQ.Context zmqContext;

  
  private BtcdClient btcdClient;

  private BtcDaemon btcDaemon;

  private Boolean supportInstantSend;
  private Boolean supportSubtractFee;
  private Boolean supportReferenceLine;

  private Map<String, ScheduledFuture<?>> unlockingTasks = new ConcurrentHashMap<>();

  private ScheduledExecutorService outputUnlockingExecutor = Executors.newSingleThreadScheduledExecutor();

  private final Object SENDING_LOCK = new Object();


  @Override
  public void initBtcdDaemon(boolean zmqEnabled)  {
    if (zmqEnabled) {
      btcDaemon = new BtcdZMQDaemonImpl(btcdClient, zmqContext);
    } else {
      btcDaemon = new BtcHttpDaemonImpl(btcdClient);
    }

    try {
      btcDaemon.init();
    } catch (Exception e) {
      //log.error(e);
      //log.error(ExceptionUtils.getStackTrace(e));
    }
  }
  
  
  @Override
  public String getNewAddress(String walletPassword) {
      Integer keyPoolSize = getKeypoolSize();

    try {

      /*
      * If wallet is encrypted and locked, pool of private keys is not refilled
      * Keys are automatically refilled on unlocking
      * */
      if (keyPoolSize < KEY_POOL_LOW_THRESHOLD) {
        unlockWallet(walletPassword, 1);
      }
      return btcdClient.getNewAddress();
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      throw new BitcoinCoreException("Cannot generate new address!");
    }
  }

    private Integer getKeypoolSize() {
        Integer keyPoolSize;
        try {
            keyPoolSize = btcdClient.getInfo().getKeypoolSize();
        } catch (BitcoindException | CommunicationException e) {
            log.error(e);
            try {
                keyPoolSize = btcdClient.getWalletInfo().getKeypoolSize();
            } catch (BitcoindException | CommunicationException e2) {
                log.error(e2);
                throw new BitcoinCoreException("Cannot generate new address!");
            }
        }
        return keyPoolSize;
    }

    @Override
  public void backupWallet(String backupFolder) {
    try {
      String filename = new StringJoiner("").add(backupFolder).add("backup_")
              .add((LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"))))
              .add(".dat").toString();
      log.debug("Backing up wallet to file: " + filename);
      btcdClient.backupWallet(filename);
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
    }
  }
  
  @Override
  public void shutdown() {
    btcdClient.close();
    btcDaemon.destroy();
  }
  
  
  private Optional<Transaction> handleConflicts(Transaction transaction) {
    if (transaction.getConfirmations() < 0 && !transaction.getWalletConflicts().isEmpty()) {
      log.warn("Wallet conflicts present");
      for (String txId : transaction.getWalletConflicts()) {
        try {
          Transaction conflicted = btcdClient.getTransaction(txId);
          if (conflicted.getConfirmations() >= 0) {
            return Optional.of(conflicted);
          }
        } catch (BitcoindException | CommunicationException e) {
          //log.error(e);
        }
      }
      return Optional.empty();
    } else {
      return Optional.of(transaction);
    }
  }
  
  @Override
  public Optional<BtcTransactionDto> handleTransactionConflicts(String txId) {
    try {
      log.debug(this);
      return handleConflicts(btcdClient.getTransaction(txId)).map(this::convert);
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
    }
    return Optional.empty();
  }
  
  private BtcTransactionDto convert(Transaction tx) {
    List<BtcTxPaymentDto> payments = tx.getDetails().stream()
            .map((payment) -> new BtcTxPaymentDto(payment.getAddress(), payment.getCategory().getName(), payment.getAmount(), payment.getFee()))
            .collect(Collectors.toList());
    return new BtcTransactionDto(tx.getAmount(), tx.getFee(), tx.getConfirmations(), tx.getTxId(), tx.getBlockHash(), tx.getWalletConflicts(), tx.getTime(),
            tx.getTimeReceived(), tx.getComment(), tx.getTo(), payments);
  }
  
  @Override
  public BtcTransactionDto getTransaction(String txId) {
    try {
      Transaction tx = btcdClient.getTransaction(txId);
      return convert(tx);
    } catch (BitcoindException | CommunicationException e) {
      throw new BitcoinCoreException(e.getMessage());
    }
  }
  
 
  @Override
  public BtcWalletInfoDto getWalletInfo() {
      BtcWalletInfoDto dto = new BtcWalletInfoDto();

      try {
          BigDecimal spendableBalance = btcdClient.getBalance("", MIN_CONFIRMATIONS_FOR_SPENDING);
          dto.setBalance(BigDecimalProcessing.formatNonePoint(spendableBalance, true));

          WalletInfo walletInfo = btcdClient.getWalletInfo();

          BigDecimal confirmedNonSpendableBalance = BigDecimalProcessing.doAction(walletInfo.getBalance(), spendableBalance, ActionType.SUBTRACT);
          BigDecimal unconfirmedBalance = btcdClient.getUnconfirmedBalance();
          dto.setConfirmedNonSpendableBalance(BigDecimalProcessing.formatNonePoint(confirmedNonSpendableBalance, true));
          dto.setUnconfirmedBalance(BigDecimalProcessing.formatNonePoint(unconfirmedBalance, true));
          dto.setTransactionCount(walletInfo.getTxCount());
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
          try {
              BigDecimal spendableBalance = btcdClient.getBalance();
              dto.setBalance(BigDecimalProcessing.formatNonePoint(spendableBalance, true));
          } catch (BitcoindException | CommunicationException e1) {
              //log.error(e1);
          }
      }
      return dto;

  }
  
  @Override
  public List<TxReceivedByAddressFlatDto> listReceivedByAddress(Integer minConfirmations) {
    try {
      List<Address> received = btcdClient.listReceivedByAddress(minConfirmations);
      return received.stream().flatMap(address -> address.getTxIds().stream().map(txId -> {
        TxReceivedByAddressFlatDto dto = new TxReceivedByAddressFlatDto();
        dto.setAccount(address.getAccount());
        dto.setAddress(address.getAddress());
        dto.setAmount(address.getAmount());
        dto.setConfirmations(address.getConfirmations());
        dto.setTxId(txId);
        return dto;
      })).collect(Collectors.toList());
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      throw new BitcoinCoreException(e.getMessage());
    }
  }
  
  @Override
  public List<BtcTransactionHistoryDto> listAllTransactions() {
    try {
      return btcdClient.listSinceBlock().getPayments().stream()
              .map(payment -> {
                BtcTransactionHistoryDto dto = new BtcTransactionHistoryDto();
                dto.setTxId(payment.getTxId());
                dto.setAddress(payment.getAddress());
                dto.setBlockhash(payment.getBlockHash());
                dto.setCategory(payment.getCategory().getName());
                dto.setAmount(BigDecimalProcessing.formatNonePoint(payment.getAmount(), true));
                dto.setFee(BigDecimalProcessing.formatNonePoint(payment.getFee(), true));
                dto.setConfirmations(payment.getConfirmations());
                dto.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(payment.getTime()), ZoneId.systemDefault()));
                return dto;
              }).collect(Collectors.toList());
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      throw new BitcoinCoreException(e.getMessage());
    }
  }


    @Override
    public List<BtcPaymentFlatDto> listSinceBlockEx(@Nullable String blockHash, Integer merchantId, Integer currencyId)  {
        try {
            return listSinceBlockExChecked(blockHash, merchantId, currencyId);
        } catch (Exception e) {
            //log.error(e);
            throw new BitcoinCoreException(e);
        }
    }

    private List<BtcPaymentFlatDto> listSinceBlockExChecked(@Nullable String blockHash, Integer merchantId, Integer currencyId) throws BitcoindException, CommunicationException {
        SinceBlock sinceBlock = blockHash == null ? btcdClient.listSinceBlock() : btcdClient.listSinceBlock(blockHash);
        return sinceBlock.getPayments().stream()
                .map(payment -> BtcPaymentFlatDto.builder()
                        .amount(payment.getAmount())
                        .confirmations(payment.getConfirmations())
                        .merchantId(merchantId)
                        .currencyId(currencyId)
                        .address(payment.getAddress())
                        .txId(payment.getTxId())
                        .blockhash(payment.getBlockHash())
                        .build()).collect(Collectors.toList());
    }



  @Override
  public List<BtcPaymentFlatDto> listSinceBlock(@Nullable String blockHash, Integer merchantId, Integer currencyId) {
    try {
      return listSinceBlockExChecked(blockHash, merchantId, currencyId);
    } catch (Exception e) {
      //log.error(e);
      return Collections.emptyList();
    }
  }
  
  
  
  @Override
  public BigDecimal estimateFee(int blockCount) {
    try {
      return btcdClient.estimateFee(blockCount);
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      try {
        SmartFee smartFee = btcdClient.estimateSmartFee(blockCount);
        if (smartFee.getErrors() != null && !smartFee.getErrors().isEmpty()) {
          return BigDecimal.valueOf(-1L);
        } else {
          return smartFee.getFeeRate();
        }
      } catch (BitcoindException | CommunicationException e1) {
        //log.error(e1);
      }
      return new BigDecimal(-1L);
    }
  }
  
  @Override
  public BigDecimal getActualFee() {
    try {
      return btcdClient.getInfo().getPayTxFee();

    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      try {
        return btcdClient.getWalletInfo().getPayTxFee();
      } catch (BitcoindException | CommunicationException e1) {
        //log.error(e1);
      }
      throw new BitcoinCoreException(e);
    }

  }
  
  @Override
  public void setTxFee(BigDecimal fee) {
    try {
      btcdClient.setTxFee(fee);
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      throw new BitcoinCoreException(e.getMessage());
    }
  }
  
  @Override
  public void submitWalletPassword(String password) {
    try {
      unlockWallet(password, 60);
    } catch (BitcoindException | CommunicationException e) {
      //log.error(e);
      throw new BitcoinCoreException(e.getMessage());
    }
  }
  
  
  /*
  * Using sendMany instead of sendToAddress allows to send only UTXO with certain number of confirmations.
  * DO NOT use immutable map creation methods like Collections.singletonMap(...), it will cause an error within lib code
  * */
  @Override
  public String sendToAddressAuto(String address, BigDecimal amount, String walletPassword) {
    
    try {
        String result;
      synchronized (SENDING_LOCK) {
      unlockWallet(walletPassword, 10);
      Map<String, BigDecimal> payments = new HashMap<>();
      payments.put(address, amount);
      if (supportReferenceLine) {
          result = btcdClient.sendMany("", payments, "", MIN_CONFIRMATIONS_FOR_SPENDING);
      } else {
          result = btcdClient.sendMany("", payments, MIN_CONFIRMATIONS_FOR_SPENDING);
      }
      lockWallet();
      }
      return result;
    } catch (BitcoindException e) {
      //log.error(e);
      if (e.getCode() == -5) {
        throw new InvalidAccountException();
      }
      if (e.getCode() == -6) {
        throw new InsufficientCostsInWalletException();
      }
      throw new MerchantException(e.getMessage());
    }
    catch (CommunicationException e) {
      //log.error(e);
      throw new MerchantException(e.getMessage());
    }
  }
  
  private void unlockWallet(String password, int authTimeout) throws BitcoindException, CommunicationException {
    unlockWallet(password, authTimeout, false);
  }

  private void forceUnlockWallet(String password, int authTimeout) throws BitcoindException, CommunicationException {
      unlockWallet(password, authTimeout, true);
  }

    private void unlockWallet(String password, int authTimeout, boolean forceUnlock) throws BitcoindException, CommunicationException {
        Long unlockedUntil = getUnlockedUntil();
        if (unlockedUntil != null && (forceUnlock || unlockedUntil == 0) ) {
            btcdClient.walletPassphrase(password, authTimeout);
        }
    }

    private void lockWallet() {
        try {
            Long unlockedUntil = getUnlockedUntil();
            if (unlockedUntil != null && unlockedUntil != 0) {
                btcdClient.walletLock();
            }
        } catch (Exception e) {
            //log.error(e);
        }
    }

    private Long getUnlockedUntil() throws BitcoindException, CommunicationException {
        try {
            return btcdClient.getInfo().getUnlockedUntil();
        } catch (Exception e) {
            //log.error(e);
            return btcdClient.getWalletInfo().getUnlockedUntil();
        }
    }

  @Override
  public BtcPaymentResultDto sendToMany(Map<String, BigDecimal> payments, boolean subtractFeeFromAmount) {
    try {
      List<String> subtractFeeAddresses = new ArrayList<>();
      if (subtractFeeFromAmount) {
        subtractFeeAddresses = new ArrayList<>(payments.keySet());
      }
      String txId;
      synchronized (SENDING_LOCK) {
          if (supportInstantSend) {
              txId = btcdClient.sendMany("", payments, MIN_CONFIRMATIONS_FOR_SPENDING, false,
                      "", subtractFeeAddresses);
          } else if (supportReferenceLine) {
              txId = btcdClient.sendMany("", payments, "", MIN_CONFIRMATIONS_FOR_SPENDING);
          }

          else {
              if (supportSubtractFee) {
                  txId = btcdClient.sendMany("", payments, MIN_CONFIRMATIONS_FOR_SPENDING,
                          "", subtractFeeAddresses);
              } else {
                  txId = btcdClient.sendMany("", payments, MIN_CONFIRMATIONS_FOR_SPENDING,"");
              }
          }
      }
      return new BtcPaymentResultDto(txId);
    } catch (Exception e) {
      //log.error(e);
      return new BtcPaymentResultDto(e);
    }
  }

  @Override
  public Flux<BtcBlockDto> blockFlux() {
    return notificationFlux("node.bitcoind.notification.block.port", btcDaemon::blockFlux, block ->
            new BtcBlockDto(block.getHash(), block.getHeight(), block.getTime()));
  }

  @Override
  public Flux<BtcTransactionDto> walletFlux() {
    return notificationFlux("node.bitcoind.notification.wallet.port", btcDaemon::walletFlux, this::convert);
  }

  @Override
  public Flux<BtcTransactionDto> instantSendFlux() {
    return notificationFlux("node.bitcoind.notification.instantsend.port", btcDaemon::instantSendFlux, this::convert);
  }

  private <S, T> Flux<T> notificationFlux(String portProperty, Function<String, Flux<S>> source, Function<S, T> mapper) {
    if (btcdClient != null) {
      String port = btcdClient.getNodeConfig().getProperty(portProperty);
      return source.apply(port).map(mapper);
    } else {
      //log.error("Client not initialized!");
      return Flux.empty();
    }
  }


    @Override
    public BtcPreparedTransactionDto prepareRawTransaction(Map<String, BigDecimal> payments) {
        return prepareRawTransaction(payments, null);
    }

    @Override
    public BtcPreparedTransactionDto prepareRawTransaction(Map<String, BigDecimal> payments, @Nullable String oldTxHex) {
        try {

          FundingResult fundingResult;
          synchronized (SENDING_LOCK) {
            if (oldTxHex != null && unlockingTasks.containsKey(oldTxHex)) {

              unlockingTasks.remove(oldTxHex).cancel(true);
              // unlock previously locked UTXO

              lockUnspentFromHex(oldTxHex, true);

            }

            String initialTxHex = btcdClient.createRawTransaction(new ArrayList<>(), payments);
            fundingResult = btcdClient.fundRawTransaction(initialTxHex);
            lockUnspentFromHex(fundingResult.getHex(), false);
          }

          unlockingTasks.put(fundingResult.getHex(), outputUnlockingExecutor.schedule(() -> {
                    // unlock UTXO after 2 minutes - in case of no action;
                    lockUnspentFromHex(fundingResult.getHex(), true);
                    log.info("Outputs unlocked for hex " + fundingResult.getHex());
                    unlockingTasks.remove(fundingResult.getHex());
                  },
                  2, TimeUnit.MINUTES));
          ;

            return new BtcPreparedTransactionDto(payments, fundingResult.getFee(), fundingResult.getHex());
        } catch (BitcoindException | CommunicationException e) {
            throw new BitcoinCoreException(e);
        }
    }


    private void lockUnspentFromHex(String hex, boolean unlock)  {
        try {
            RawTransactionOverview txOverview = btcdClient.decodeRawTransaction(hex);
            btcdClient.lockUnspent(unlock, txOverview.getVIn().stream()
                    .map(vin -> new OutputOverview(vin.getTxId(), vin.getVOut())).collect(Collectors.toList()));
        } catch (BitcoindException | CommunicationException e) {
            //log.error(e);
        }
    }

    @Override
  public BtcPaymentResultDto signAndSendRawTransaction(String hex) {
      try {
          checkLockStateForRawTx(hex);
          SignatureResult signatureResult = btcdClient.signRawTransaction(hex);
          if (!signatureResult.getComplete()) {
              throw new BitcoinCoreException("Signature failed!");
          }
          String txId = btcdClient.sendRawTransaction(signatureResult.getHex(), false);
          return new BtcPaymentResultDto(txId);
      } catch (Exception e) {
          //log.error(e);
          return new BtcPaymentResultDto(e);
      }
  }


  private void checkLockStateForRawTx(String hex) {
      try {
          Set<BtcTxOutputDto> lockedOutputSet = btcdClient.listLockUnspent().stream()
                  .map(out -> new BtcTxOutputDto(out.getTxId(), out.getVOut())).collect(Collectors.toSet());

           boolean txOutputsLocked = btcdClient.decodeRawTransaction(hex).getVIn().stream()
                  .map(vin -> new BtcTxOutputDto(vin.getTxId(), vin.getVOut())).allMatch(lockedOutputSet::contains);

           if (!txOutputsLocked) {
               throw new IllegalStateException("Transaction outputs already unlocked!");
           }

      } catch (BitcoindException | CommunicationException e) {
          //log.error(e);
      }
  }

  @Override
  public String getTxIdByHex(String hex) {
    try {
      return btcdClient.decodeRawTransaction(hex).getTxId();
    } catch (BitcoindException | CommunicationException e) {
      throw new BitcoinCoreException("Cannot decode tx " + hex, e);
    }
  }

  @Override
  public String getLastBlockHash() {
      try {
          return btcdClient.getBestBlockHash();
      } catch (BitcoindException | CommunicationException e) {
          //log.error(e);
          try {
              return btcdClient.getBlockHash(btcdClient.getBlockCount());
          } catch (BitcoindException | CommunicationException e1) {
              //log.error(e1);
          }
          throw new BitcoinCoreException(e);
      }
  }

  @Override
  public BtcBlockDto getBlockByHash(String blockHash) {
      try {
          Block block = btcdClient.getBlock(blockHash);
          return new BtcBlockDto(block.getHash(), block.getHeight(), block.getTime());
      } catch (BitcoindException | CommunicationException e) {
          //log.error(e);
          throw new BitcoinCoreException(e);
      }
  }

    @Override
    public long getBlocksCount() throws BitcoindException, CommunicationException {
        return btcdClient.getBlockCount();
    }

    @Override
    public Long getLastBlockTime() throws BitcoindException, CommunicationException {
        return btcdClient.getBlock(btcdClient.getBestBlockHash()).getTime();
    }

    @Override
    public void initCoreClient(BitcoinNode node, boolean supportSubtractFee, boolean supportReferenceLine) {
        final String rpcProtocol = node.getRpcProtocol();
        final String rpcHost = node.getRpcHost();
        final String rpcPort = node.getRpcPort();
        final String rpcUser = node.getRpcUser();
        final String rpcPassword = node.getRpcPassword();
        final String httpAuthSchema = node.getHttpAuthSchema();
        final String notificationAlertPort = node.getNotificationAlertPort();
        final String notificationBlockPort = node.getNotificationBlockPort();
        final String notificationWalletPort = node.getNotificationWalletPort();
        final String notificationInstantSendPort = node.getNotificationInstantSendPort();
        try {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            double timeout = 10;
            RequestConfig config = RequestConfig.custom().
                    setConnectTimeout(10 * 1000).
                    setConnectionRequestTimeout(10 * 1000).
                    setSocketTimeout(10 * 1000).build();

            CloseableHttpClient httpProvider = HttpClients.custom()
                    .setConnectionManager(cm)
                    .setDefaultRequestConfig(config)
                    .build();
            Properties nodeConfig = new Properties();
            if (nonNull(rpcProtocol)) {
                nodeConfig.setProperty("node.bitcoind.rpc.protocol", rpcProtocol);
            }
            if (nonNull(rpcHost)) {
                nodeConfig.setProperty("node.bitcoind.rpc.host", rpcHost);
            }
            if (nonNull(rpcPort)) {
                nodeConfig.setProperty("node.bitcoind.rpc.port", rpcPort);
            }
            if (nonNull(rpcUser)) {
                nodeConfig.setProperty("node.bitcoind.rpc.user", rpcUser);
            }
            if (nonNull(rpcPassword)) {
                nodeConfig.setProperty("node.bitcoind.rpc.password", rpcPassword);
            }
            if (nonNull(httpAuthSchema)) {
                nodeConfig.setProperty("node.bitcoind.http.auth_scheme", httpAuthSchema);
            }
            if (nonNull(notificationAlertPort)) {
                nodeConfig.setProperty("node.bitcoind.notification.alert.port", notificationAlertPort);
            }
            if (nonNull(notificationBlockPort)) {
                nodeConfig.setProperty("node.bitcoind.notification.block.port", notificationBlockPort);
            }
            if (nonNull(notificationWalletPort)) {
                nodeConfig.setProperty("node.bitcoind.notification.wallet.port", notificationWalletPort);
            }
            if (nonNull(notificationInstantSendPort)) {
                nodeConfig.setProperty("node.bitcoind.notification.instantsend.port", notificationInstantSendPort);
            }
            log.info("Node config: " + nodeConfig);
            btcdClient = new BtcdClientImpl(httpProvider, nodeConfig);
            this.supportInstantSend = node.isSupportInstantSend();
            this.supportSubtractFee = supportSubtractFee;
            this.supportReferenceLine = supportReferenceLine;
        } catch (Exception ex) {
            //log.error("Could not initialize BTCD client. Reason:", ex.getMessage());
            //log.error(ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public PagingData<List<BtcTransactionHistoryDto>> listTransaction(int start, int length, String searchValue){
        try {
            PagingData<List<BtcTransactionHistoryDto>> result = new PagingData<>();

            int recordsTotal = getWalletInfo().getTransactionCount();
            List<BtcTransactionHistoryDto> data = getTransactionsForPagination(start, length);

            if(!(StringUtils.isEmpty(searchValue))){
                recordsTotal = findTransactions(searchValue).size();
                data = findTransactions(searchValue);
            }
            result.setData(data);
            result.setTotal(recordsTotal);
            result.setFiltered(recordsTotal);

            return result;
        } catch (BitcoindException | CommunicationException e) {
            log.error(e);
            throw new BitcoinCoreException(e.getMessage());
        }
    }

    @Override
    public List<BtcTransactionHistoryDto> getTransactionsForPagination(int start, int length) throws BitcoindException, CommunicationException {
        return ImmutableList.copyOf(btcdClient.listTransactions("", length, start).stream()
                .map(payment -> {
                    BtcTransactionHistoryDto dto = new BtcTransactionHistoryDto();
                    dto.setTxId(payment.getTxId());
                    dto.setAddress(payment.getAddress());
                    dto.setBlockhash(payment.getBlockHash());
                    dto.setCategory(payment.getCategory().getName());
                    dto.setAmount(BigDecimalProcessing.formatNonePoint(payment.getAmount(), true));
                    dto.setFee(BigDecimalProcessing.formatNonePoint(payment.getFee(), true));
                    dto.setConfirmations(payment.getConfirmations());
                    dto.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(payment.getTime()), ZoneId.systemDefault()));
                    return dto;
                }).collect(Collectors.toList())).reverse();
    }

    @Override
    public List<BtcTransactionHistoryDto> getTransactionsByPage(int page, int transactionsPerPage) throws BitcoindException, CommunicationException {
        return btcdClient.listTransactions("", transactionsPerPage, page * transactionsPerPage).stream()
                .map(payment -> {
                    BtcTransactionHistoryDto dto = new BtcTransactionHistoryDto();
                    dto.setTxId(payment.getTxId());
                    dto.setAddress(payment.getAddress());
                    dto.setBlockhash(payment.getBlockHash());
                    dto.setCategory(payment.getCategory().getName());
                    dto.setAmount(BigDecimalProcessing.formatNonePoint(payment.getAmount(), true));
                    dto.setFee(BigDecimalProcessing.formatNonePoint(payment.getFee(), true));
                    dto.setConfirmations(payment.getConfirmations());
                    dto.setTime(LocalDateTime.ofInstant(Instant.ofEpochSecond(payment.getTime()), ZoneId.systemDefault()));
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<BtcTransactionHistoryDto> findTransactions(String value) throws BitcoindException, CommunicationException {

        List<BtcTransactionHistoryDto> result = new ArrayList<>();
        List<BtcTransactionHistoryDto> transactions;

        for (int i = 0; (transactions = getTransactionsByPage(i, TRANSACTIONS_PER_PAGE_FOR_SEARCH)).size() > 0; i++){
            List<BtcTransactionHistoryDto> matches = transactions.stream().filter(e ->
                    (StringUtils.equals(e.getAddress(), value)) || StringUtils.equals(e.getBlockhash(), value) || StringUtils.equals(e.getTxId(), value))
                    .collect(Collectors.toList());
            result.addAll(matches);
        }

        return result;
    }

    @PreDestroy
  private void shutDown() {
    outputUnlockingExecutor.shutdown();
  }




}

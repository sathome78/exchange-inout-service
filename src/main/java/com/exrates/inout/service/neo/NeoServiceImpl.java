package com.exrates.inout.service.neo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.neo.AssetMerchantCurrencyDto;
import com.exrates.inout.domain.neo.NeoAsset;
import com.exrates.inout.domain.neo.NeoTransaction;
import com.exrates.inout.domain.neo.NeoVout;
import com.exrates.inout.domain.other.ProfileData;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.properties.models.NeoProperty;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.util.ParamMapUtils;
import com.exrates.inout.util.WithdrawUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


//@Log4j2(topic = "neo_log")
@PropertySource("classpath:/merchants/neo.properties")
public class NeoServiceImpl implements NeoService {

   private static final Logger log = LogManager.getLogger("neo_log");


    private static final String NEO_SPEC_PARAM_NAME = "LastRecievedBlock";

    @Autowired
    private RefillService refillService;
    @Autowired
    private MerchantSpecParamsDao specParamsDao;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WithdrawUtils withdrawUtils;

    private String merchantName;
    private String currencyName;
    private int minConfirmations;

    private String endpoint;
    private String address;

    private Merchant merchant;
    private Currency currency;
    private Map<String, AssetMerchantCurrencyDto> neoAssetMap;

    private NeoNodeService neoNodeService;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    public void init() {
        this.neoNodeService = new NeoNodeServiceImpl(endpoint, restTemplate, objectMapper);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                scanLastBlocksAndUpdatePayments();
            } catch (Exception e) {
                log.error(e);
            }
        }, 3L, 5L, TimeUnit.MINUTES);
    }

    public NeoServiceImpl(NeoProperty property) {
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.minConfirmations = property.getMinConfirmations();
        this.endpoint = property.getEndpoint();
        this.address = property.getAddress();
        this.merchant = property.getMerchant();
        this.currency = property.getCurrency();
        this.neoAssetMap = property.getNeoAssetMap();
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String address = neoNodeService.getNewAddress();
        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[]{address}, request.getLocale());
        return new HashMap<String, String>() {{
            put("message", message);
            put("address", address);
            put("qr", address);
        }};
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        String txId = ParamMapUtils.getIfNotNull(params, "txId");
        String address = ParamMapUtils.getIfNotNull(params, "address");
        NeoTransaction transaction = neoNodeService.getTransactionById(txId).orElseThrow(() -> new NeoPaymentProcessingException("Transaction not found: " + txId));
        NeoVout vout = transaction.getVout().stream().filter(out -> address.equals(out.getAddress()))
                .findFirst().orElseThrow(() -> new NeoPaymentProcessingException(String.format("Tx %s does not contain address %s", txId, address)));
        processNeoPayment(txId, vout, transaction.getConfirmations(), transaction.getBlockhash());
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        BigDecimal withdrawAmount = new BigDecimal(withdrawMerchantOperationDto.getAmount());
        NeoAsset asset = NeoAsset.valueOf(withdrawMerchantOperationDto.getCurrency());
        try {
            NeoTransaction neoTransaction = neoNodeService.sendToAddress(asset, withdrawMerchantOperationDto.getAccountTo(), withdrawAmount, address);
            return Collections.singletonMap("hash", neoTransaction.getTxid());
        } catch (NeoApiException e) {
            if (e.getCode() == -300) {
                throw new InsufficientCostsInWalletException();
            } else if (e.getCode() == -2146233033) {
                throw new InvalidAccountException();
            } else {
                throw new MerchantException(e.getMessage());
            }
        } catch (Exception e) {
            throw new MerchantException(e);
        }
    }

    @Override
    public void scanLastBlocksAndUpdatePayments() {
        log.debug("Start scanning blocks");
        ProfileData profileData = new ProfileData(500);
        scanBlocks();
        profileData.setTime1();
        updateExistingPayments();
        profileData.setTime2();
        log.debug("Profile results: " + profileData);
    }

    private void scanBlocks() {
        final int lastReceivedBlock = Integer.parseInt(specParamsDao.getByMerchantNameAndParamName(merchant.getName(),
                NEO_SPEC_PARAM_NAME).getParamValue());
        final int blockCount = neoNodeService.getBlockCount();
        Set<String> addresses = new HashSet<>(refillService.findAllAddresses(merchant.getId(), currency.getId()));
        List<NeoVout> outputs = IntStream.range(lastReceivedBlock, blockCount).parallel().mapToObj(neoNodeService::getBlock).filter(Optional::isPresent).map(Optional::get)
                .flatMap(block -> block.getTx().stream().filter(tx -> "ContractTransaction".equals(tx.getType()))
                        .flatMap(tx -> tx.getVout().stream().filter(vout -> addresses.contains(vout.getAddress()))
                                .filter(vout -> neoAssetMap.containsKey(vout.getAsset()))
                                .peek(vout -> {
                                    try {
                                        processNeoPayment(tx.getTxid(), vout, block.getConfirmations(), block.getHash());
                                    } catch (Exception e) {
                                        log.error(e);
                                    }
                                }))
                ).collect(Collectors.toList());
        log.debug("Processed outputs: " + outputs);
        specParamsDao.updateParam(merchant.getName(), NEO_SPEC_PARAM_NAME, String.valueOf(blockCount));
    }

    private void updateExistingPayments() {
        log.debug("Check and update existing payments");
        neoAssetMap.forEach((key, value) ->
                refillService.getInExamineByMerchantIdAndCurrencyIdList(value.getMerchant().getId(), value.getCurrency().getId())
                        .stream().flatMap(dto -> Stream.of(neoNodeService.getTransactionById(dto.getMerchantTransactionId())).filter(Optional::isPresent).map(Optional::get)
                        .flatMap(tx -> tx.getVout().stream().filter(vout -> dto.getAddress().equals(vout.getAddress())).peek(vout -> {
                            try {
                                changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                                        .requestId(dto.getId())
                                        .address(vout.getAddress())
                                        .amount(new BigDecimal(vout.getValue()))
                                        .confirmations(tx.getConfirmations())
                                        .currencyId(dto.getCurrencyId())
                                        .merchantId(dto.getMerchantId())
                                        .hash(dto.getMerchantTransactionId())
                                        .blockhash(tx.getBlockhash()).build(), vout.getAsset());
                            } catch (Exception e) {
                                log.error(e);
                            }
                        }))).forEach(vout -> log.debug("Payment updated: " + vout)));
    }

    private void processNeoPayment(String txId, NeoVout vout, Integer confirmations, String blockhash) {
        AssetMerchantCurrencyDto assetMerchantCurrencyDto = neoAssetMap.get(vout.getAsset());
        String address = vout.getAddress();
        BigDecimal amount = new BigDecimal(vout.getValue());
        int merchantId = assetMerchantCurrencyDto.getMerchant().getId();
        int currencyId = assetMerchantCurrencyDto.getCurrency().getId();

        Optional<Integer> refillRequestIdOnExamResult = refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(address,
                merchantId, currencyId, txId);
        if (refillRequestIdOnExamResult.isPresent()) {
            Integer refillRequestIdOnExam = refillRequestIdOnExamResult.get();
            changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                    .requestId(refillRequestIdOnExam)
                    .address(address)
                    .amount(amount)
                    .confirmations(confirmations)
                    .currencyId(currencyId)
                    .merchantId(merchantId)
                    .hash(txId)
                    .blockhash(blockhash).build(), vout.getAsset());
        } else {
            Optional<Integer> refillRequestIdResult = refillService.getRequestIdInPendingByAddressAndMerchantIdAndCurrencyId(
                    address, merchantId, currencyId);
            Integer requestId = refillRequestIdResult.orElseGet(() -> {
                RefillRequestAcceptDto refillRequestAcceptDto = RefillRequestAcceptDto.builder()
                        .address(address)
                        .amount(amount)
                        .merchantId(merchantId)
                        .currencyId(currencyId)
                        .merchantTransactionId(txId).build();

                log.debug("Create request by fact! : " + refillRequestAcceptDto);
                return refillService.createRefillRequestByFact(refillRequestAcceptDto);
            });
            if (confirmations >= 0 && confirmations < minConfirmations) {
                try {
                    refillService.putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto.builder()
                            .requestId(requestId)
                            .merchantId(merchantId)
                            .currencyId(currencyId)
                            .address(address)
                            .amount(amount)
                            .hash(txId)
                            .blockhash(blockhash).build());
                } catch (RefillRequestAppropriateNotFoundException e) {
                    log.error(e);
                }
            } else {
                changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto.builder()
                        .requestId(requestId)
                        .address(address)
                        .amount(amount)
                        .confirmations(confirmations)
                        .currencyId(currencyId)
                        .merchantId(merchantId)
                        .hash(txId)
                        .blockhash(blockhash).build(), vout.getAsset());
            }
        }
    }

    private void changeConfirmationsOrProvide(RefillRequestSetConfirmationsNumberDto dto, String assetId) {
        try {
            if (dto.getConfirmations() != null) {
                refillService.setConfirmationCollectedNumber(dto);
                if (dto.getConfirmations() >= minConfirmations) {
                    log.debug("Providing transaction!");
                    RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                            .requestId(dto.getRequestId())
                            .address(dto.getAddress())
                            .amount(dto.getAmount())
                            .currencyId(dto.getCurrencyId())
                            .merchantId(dto.getMerchantId())
                            .merchantTransactionId(dto.getHash())
                            .build();
                    refillService.autoAcceptRefillRequest(requestAcceptDto);
                    transferCostsToMainAccount(assetId, dto.getAmount());
                }
            }
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error(e);
        }
    }

    private void transferCostsToMainAccount(String assetId, BigDecimal amount) {
        neoNodeService.sendToAddress(neoAssetMap.get(assetId).getAsset(), address, amount, address);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }
}
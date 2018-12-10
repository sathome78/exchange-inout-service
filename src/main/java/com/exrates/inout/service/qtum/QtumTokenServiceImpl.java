package com.exrates.inout.service.qtum;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.other.ProfileData;
import com.exrates.inout.domain.qtum.QtumTokenTransaction;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.models.QtumProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.util.ExConvert;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "qtum_log")
public class QtumTokenServiceImpl {

    private static final String QTUM_SPEC_PARAM_NAME = "LastRecievedBlock";
    private static final BigDecimal AMOUNT_FOR_COMMISSION = new BigDecimal("0.15");

    @Value("${qtum.min-confirmations}")
    private int minConfirmations;
    @Value("${qtum.min-transfer-amount}")
    private int minTransferAmount;
    @Value("${qtum.main-address-for-transfer}")
    private String mainAddressForTransfer;

    @Autowired
    private QtumNodeService qtumNodeService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MerchantSpecParamsDao specParamsDao;
    @Autowired
    private RefillService refillService;

    private List<String> contractAddress;
    private String merchantName;
    private String currencyName;
    private ExConvert.Unit unit;

    private Merchant merchant;
    private Currency currency;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public QtumTokenServiceImpl(QtumProperty property) {
        this.contractAddress = Arrays.asList(property.getContract().replaceAll(" ", "").split(","));
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.unit = property.getUnit();
    }

    @PostConstruct
    private void init() {
        merchant = merchantService.findByName(merchantName);
        currency = currencyService.findByName(currencyName);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                scanBlocks();
            } catch (Exception e) {
                log.error(e);
            }
        }, 8L, 30, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkBalanceAndTransfer();
            } catch (Exception e) {
                log.error(e);
            }
        }, 16L, 125L, TimeUnit.MINUTES);
    }

    @Synchronized
    private void scanBlocks() {
        log.debug("Start scanning blocks QtumToken");
        ProfileData profileData = new ProfileData(500);

        final int lastReceivedBlock = Integer.parseInt(specParamsDao.getByMerchantNameAndParamName(merchant.getName(),
                QTUM_SPEC_PARAM_NAME).getParamValue());

        Set<String> addresses = new HashSet<>(refillService.findAllAddresses(merchant.getId(), currency.getId()));

        List<QtumTokenTransaction> tokenTransactions = qtumNodeService.getTokenHistory(lastReceivedBlock, contractAddress);
        log.info("token transactions:" + tokenTransactions.toString());
        tokenTransactions.stream()
                .filter(t -> addresses.contains(extractLogs(t.getLog().get(0).getTopics(), t.getLog().get(0).getData()).to))
                .filter(t -> !refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(extractLogs(t.getLog().get(0).getTopics(), t.getLog().get(0).getData()).to
                        , merchant.getId(), currency.getId(), t.getTransactionHash()).isPresent())
                .filter(t -> qtumNodeService.getBlock(t.getBlockHash()).getConfirmations() >= minConfirmations)
                .forEach(t -> {
                    log.info("before processPayment");
                    QtumTokenServiceImpl.TransferEventResponse transferEventResponse = extractLogs(t.getLog().get(0).getTopics(), t.getLog().get(0).getData());

                    try {
                        Map<String, Object> mapPayment = new HashMap<>();
                        mapPayment.put("address", transferEventResponse.to);
                        mapPayment.put("hash", t.getTransactionHash());
                        mapPayment.put("amount", transferEventResponse.amount);
                        processPayment(mapPayment);
                        log.info("after processPayment");

                        specParamsDao.updateParam(merchant.getName(), QTUM_SPEC_PARAM_NAME, String.valueOf(t.getBlockNumber() + 1));
                    } catch (Exception e) {
                        log.error(e);
                    }

                });
        profileData.setTime1();
        log.debug("Profile results: " + profileData);
    }

    public void processPayment(Map<String, Object> params) throws RefillRequestAppropriateNotFoundException {
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(String.valueOf(params.get("address")))
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount((BigDecimal) params.get("amount"))
                .merchantTransactionId((String) params.get("hash"))
                .build();

        Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
        requestAcceptDto.setRequestId(requestId);
        refillService.autoAcceptRefillRequest(requestAcceptDto);
        refillService.updateAddressNeedTransfer(String.valueOf(params.get("address")), merchant.getId(),
                currency.getId(), true);
    }

    private void checkBalanceAndTransfer() {
        log.debug("Start checking balance");
        ProfileData profileData = new ProfileData(500);

        List<RefillRequestAddressDto> listRefillRequestAddressDto = refillService.findAllAddressesNeededToTransfer(merchant.getId(), currency.getId());
        log.info("listRefillRequestAddressDto: " + listRefillRequestAddressDto.toString());
        listRefillRequestAddressDto
                .forEach(t ->
                        {
                            log.info("start sending token balance");
                            String balancePrefix = "70a08231";
                            String hexAddressFrom = TypeEncoder.encode(new Address(qtumNodeService.getHexAddress(t.getAddress())));
                            String balanceData = balancePrefix + hexAddressFrom;
                            String hexBalance = qtumNodeService.getTokenBalance(contractAddress.get(0), balanceData).getExecutionResult().getOutput();
                            BigInteger balance = (BigInteger) FunctionReturnDecoder.decodeIndexedValue(hexBalance, new TypeReference<Uint256>() {
                            }).getValue();
                            log.info("token balance: " + balance.toString());

                            if (balance.compareTo(ExConvert.toWei(minTransferAmount, unit).toBigInteger()) > 0) {
                                try {
                                    qtumNodeService.setWalletPassphrase();
                                    qtumNodeService.transfer(t.getAddress(), AMOUNT_FOR_COMMISSION);

                                    String transferPrefix = "a9059cbb";
                                    String hexAddressTo = TypeEncoder.encode(new Address(qtumNodeService.getHexAddress(mainAddressForTransfer)));
                                    String hexAmountForTransfer = TypeEncoder.encode(new Uint256(balance));
                                    String transferData = transferPrefix + hexAddressTo + hexAmountForTransfer;
                                    qtumNodeService.setWalletPassphrase();
                                    log.info("before token transfer");
                                    log.info(contractAddress.get(0));
                                    log.info(transferData);
                                    log.info(t.getAddress());
                                    qtumNodeService.sendToContract(contractAddress.get(0), transferData, t.getAddress());
                                    log.info("after token transfer");
                                } catch (Exception e) {
                                    log.error(e);
                                }
                            }
                            refillService.updateAddressNeedTransfer(t.getAddress(), merchant.getId(),
                                    currency.getId(), false);
                        }
                );
        profileData.setTime1();
        log.debug("Profile results: " + profileData);
    }

    private QtumTokenServiceImpl.TransferEventResponse extractLogs(List<String> topics, String data) {
        final Event event = new Event("Transfer",
                Arrays.asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        String encodedEventSignature = Numeric.cleanHexPrefix(EventEncoder.encode(event));
        if (!topics.get(0).equals(encodedEventSignature)) {
            return null;
        }
        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues = FunctionReturnDecoder.decode(
                data, event.getNonIndexedParameters());
        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value = FunctionReturnDecoder.decodeIndexedValue(
                    topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        EventValues eventValues = new EventValues(indexedValues, nonIndexedValues);
        QtumTokenServiceImpl.TransferEventResponse typedResponse = new QtumTokenServiceImpl.TransferEventResponse();
        typedResponse.from = qtumNodeService.fromHexAddress(Numeric.cleanHexPrefix(eventValues.getIndexedValues().get(0).toString()));
        typedResponse.to = qtumNodeService.fromHexAddress(Numeric.cleanHexPrefix(eventValues.getIndexedValues().get(1).toString()));
        typedResponse.amount = ExConvert.fromWei(String.valueOf(eventValues.getNonIndexedValues().get(0).getValue()), unit);

        return typedResponse;
    }

    static class TransferEventResponse {

        public String from;
        public String to;
        public BigDecimal amount;
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }
}
package com.exrates.inout.service.ethereum;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.EthereumException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.models.EthereumTokenProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.ethereum.ethTokensWrappers.ethTokenERC20;
import com.exrates.inout.service.ethereum.ethTokensWrappers.ethTokenNotERC20;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "eth_tokens_log")
@Service
public class EthTokenServiceImpl implements EthTokenService {

    private Merchant merchant;
    private Currency currency;
    private List<String> contractAddress;
    private String merchantName;
    private String currencyName;
    private int minConfirmations;
    private BigInteger currentBlockNumber;
    private List<RefillRequestFlatDto> pendingTransactions;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private final BigInteger GAS_LIMIT = BigInteger.valueOf(180000);

    private final BigDecimal feeAmount = new BigDecimal("0.01");

    private final BigDecimal minBalanceForTransfer = new BigDecimal("0.1");

    private final boolean isERC20;

    private final ExConvert.Unit unit;

    private final BigInteger minWalletBalance;

    @Autowired
    private RefillService refillService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MerchantService merchantService;

    @Qualifier(value = "ethereumServiceImpl")
    @Autowired
    private EthereumCommonService ethereumCommonService;

    @Override
    public Integer currencyId() {
        return currency.getId();
    }

    public EthTokenServiceImpl(EthereumTokenProperty property) {
        this.contractAddress = Arrays.asList(property.getContract().replaceAll(" ", "").split(","));
        this.merchantName = property.getMerchantName();
        this.currencyName = property.getCurrencyName();
        this.isERC20 = property.isERC20();
        this.unit = property.getUnit();
        this.minWalletBalance = property.getMinWalletBalance().toBigInteger();
    }

    @PostConstruct
    public void init() {
        merchant = merchantService.findByName(merchantName);
        currency = currencyService.findByName(currencyName);

        currentBlockNumber = new BigInteger("0");
        pendingTransactions = refillService.getInExamineByMerchantIdAndCurrencyIdList(merchant.getId(), currency.getId());
        this.minConfirmations = ethereumCommonService.minConfirmationsRefill();

        scheduler.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    transferFundsToMainAccount();
                }catch (Exception e){
                    log.error(e);
                }
            }
        }, 3, 25, TimeUnit.MINUTES);
    }

    @Override
    public List<String> getContractAddress(){
        return contractAddress;
    }

    @Override
    public void tokenTransaction(Transaction transaction){
        try {
            /*check unconfirmed transactions*/
            checkTransaction(transaction.getBlockNumber());

            TransactionReceipt transactionReceipt = new TransactionReceipt();
            transactionReceipt = ethereumCommonService.getWeb3j().ethGetTransactionReceipt(transaction.getHash()).send().getResult();
            if (transactionReceipt == null) {
                log.error("receipt null " + transaction.getHash());
                return;
            }
            List<Log> logsList = transactionReceipt.getLogs();
            logsList.forEach(l -> {
                TransferEventResponse response = extractData(l.getTopics(), l.getData());
                if (response == null) {
                    log.trace("response null " + transaction.getHash());
                    return;
                }

                String contractRecipient = response.to.toString();
                if (ethereumCommonService.getAccounts().contains(contractRecipient)){
                    if (!refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(
                            contractRecipient,
                            merchant.getId(),
                            currency.getId(),
                            transaction.getHash()).isPresent()){
                        BigDecimal amount = ExConvert.fromWei(response.value.getValue().toString(), unit);
                        log.debug(merchant.getName() + " recipient: " + contractRecipient + ", amount: " + amount);

                        Integer requestId = refillService.createRefillRequestByFact(RefillRequestAcceptDto.builder()
                                .address(contractRecipient)
                                .amount(amount)
                                .merchantId(merchant.getId())
                                .currencyId(currency.getId())
                                .merchantTransactionId(transaction.getHash()).build());
                        try {
                            refillService.putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto.builder()
                                    .requestId(requestId)
                                    .merchantId(merchant.getId())
                                    .currencyId(currency.getId())
                                    .address(contractRecipient)
                                    .amount(amount)
                                    .hash(transaction.getHash())
                                    .blockhash(transaction.getBlockNumber().toString()).build());
                        } catch (RefillRequestAppropriateNotFoundException e) {
                            log.error(e);
                        }
                        pendingTransactions.add(refillService.getFlatById(requestId));
                    }
                }
            });
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Synchronized
    @Override
    public void checkTransaction(BigInteger txBlock) {
        if (!currentBlockNumber.equals(txBlock)){
            log.debug(merchant.getName() + " Current block number: " + txBlock.toString());

            List<RefillRequestFlatDto> providedTransactions = new ArrayList<RefillRequestFlatDto>();
            pendingTransactions.forEach(pendingTransaction ->
                    {
                        try {
                            if (ethereumCommonService.getWeb3j().ethGetTransactionByHash(pendingTransaction.getMerchantTransactionId()).send().getResult()==null){
                                return;
                            }
                            BigInteger transactionBlockNumber = ethereumCommonService.getWeb3j().ethGetTransactionByHash(pendingTransaction.getMerchantTransactionId()).send().getResult().getBlockNumber();
                            if (txBlock.subtract(transactionBlockNumber).intValue() > minConfirmations){

                                provideTransactionAndTransferFunds(pendingTransaction.getAddress(), pendingTransaction.getMerchantTransactionId());
                                ethereumCommonService.saveLastBlock(txBlock.toString());
                                log.debug(merchant.getName() + " Transaction: " + pendingTransaction + " - PROVIDED!!!");
                                log.debug(merchant.getName() + " Confirmations count: " + txBlock.subtract(transactionBlockNumber).intValue());
                                providedTransactions.add(pendingTransaction);
                            }
                        } catch (EthereumException | IOException e) {
                            log.error(merchant.getName() + " " + e);
                        }
                    }

            );
            providedTransactions.forEach(pendingTransaction -> pendingTransactions.remove(pendingTransaction));
        }
        currentBlockNumber = txBlock;
    }

    private void provideTransactionAndTransferFunds(String address, String merchantTransactionId){

        try {
            Optional<RefillRequestBtcInfoDto> refillRequestInfoDto = refillService.findRefillRequestByAddressAndMerchantTransactionId(address, merchantTransactionId, merchantName, currencyName);
            if (!refillRequestInfoDto.get().getStatus().equalsIgnoreCase(RefillStatusEnum.ON_BCH_EXAM.name())) {
                return;
            }
            log.debug("Providing transaction!");
            RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                    .requestId(refillRequestInfoDto.get().getId())
                    .address(refillRequestInfoDto.get().getAddress())
                    .amount(refillRequestInfoDto.get().getAmount())
                    .currencyId(currencyService.findByName(currencyName).getId())
                    .merchantId(merchantService.findByName(merchantName).getId())
                    .merchantTransactionId(merchantTransactionId)
                    .build();
            refillService.autoAcceptRefillRequest(requestAcceptDto);
            log.debug(merchantName + " Ethereum transaction " + requestAcceptDto.toString() + " --- PROVIDED!!!");

            refillService.updateAddressNeedTransfer(requestAcceptDto.getAddress(), merchant.getId(), currency.getId(), true);

        } catch (Exception e) {
            log.error(e);
        }

    }

    private void transferFundsToMainAccount(){
        List<RefillRequestAddressDto> listRefillRequestAddressDto = refillService.findAllAddressesNeededToTransfer(merchant.getId(), currency.getId());
        for (RefillRequestAddressDto refillRequestAddressDto : listRefillRequestAddressDto){
            try {
                log.info("Start method transferFundsToMainAccount...");
                Credentials credentials = Credentials.create(new ECKeyPair(new BigInteger(refillRequestAddressDto.getPrivKey()),
                        new BigInteger(refillRequestAddressDto.getPubKey())));
                BigInteger GAS_PRICE = ethereumCommonService.getWeb3j().ethGasPrice().send().getGasPrice();
                /*BigInteger GAS_PRICE = new BigInteger("21000000000");*/

                Class clazz = Class.forName("me.exrates.service.ethereum.ethTokensWrappers." + merchantName);
                Method method = clazz.getMethod("load", String.class, Web3j.class, Credentials.class, BigInteger.class, BigInteger.class);

                if (isERC20){

                    ethTokenERC20 contract = (ethTokenERC20)method.invoke(null, contractAddress.get(0), ethereumCommonService.getWeb3j(), credentials, GAS_PRICE, GAS_LIMIT);
                    ethTokenERC20 contractMain = (ethTokenERC20)method.invoke(null, contractAddress.get(0), ethereumCommonService.getWeb3j(), ethereumCommonService.getCredentialsMain(), GAS_PRICE, GAS_LIMIT);

                    BigInteger balance = contract.balanceOf(credentials.getAddress()).send();
                    BigDecimal ethBalance = Convert.fromWei(String.valueOf(ethereumCommonService.getWeb3j().ethGetBalance(refillRequestAddressDto.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance()), Convert.Unit.ETHER);

                    if (balance.compareTo(ExConvert.toWei(minBalanceForTransfer, unit).toBigInteger()) <= 0){
                        refillService.updateAddressNeedTransfer(refillRequestAddressDto.getAddress(), merchant.getId(),
                                currency.getId(), false);

                        if ( ethBalance.compareTo(new BigDecimal("0.001")) > 0) {
                            Transfer.sendFunds(
                                    ethereumCommonService.getWeb3j(), credentials, ethereumCommonService.getTransferAccAddress(), ethBalance
                                            .subtract(Convert.fromWei(Transfer.GAS_LIMIT.multiply(ethereumCommonService.getWeb3j().ethGasPrice().send().getGasPrice()).toString(), Convert.Unit.ETHER)), Convert.Unit.ETHER).sendAsync();
                        }
                        continue;
                    }

                    BigInteger futureAllowance = contract.allowance(credentials.getAddress(),
                            ethereumCommonService.getCredentialsMain().getAddress()).send();
                    if (futureAllowance.compareTo(balance) < 0 && ethBalance.compareTo(feeAmount) < 0) {
                        Transfer.sendFunds(
                                ethereumCommonService.getWeb3j(), ethereumCommonService.getCredentialsMain(),
                                credentials.getAddress(), feeAmount, Convert.Unit.ETHER).sendAsync();

                        contract.approve(ethereumCommonService.getCredentialsMain().getAddress(), ExConvert.toWei(new BigDecimal("500000000"), unit).toBigInteger()).send();
                    }else if (futureAllowance.compareTo(balance) < 0){
                        contract.approve(ethereumCommonService.getCredentialsMain().getAddress(), ExConvert.toWei(new BigDecimal("500000000"), unit).toBigInteger()).send();
                    }

                    contractMain.transferFrom(credentials.getAddress(),
                            ethereumCommonService.getMainAddress(), balance.subtract(minWalletBalance)).send();

                    log.debug(merchantName + " Funds " + ExConvert.fromWei(String.valueOf(balance.subtract(minWalletBalance)), unit) + " sent to main account!!!");
                }else {

                    ethTokenNotERC20 contract = (ethTokenNotERC20)method.invoke(null, contractAddress.get(0), ethereumCommonService.getWeb3j(), credentials, GAS_PRICE, GAS_LIMIT);

                    BigInteger balance = contract.balanceOf(credentials.getAddress()).send();
                    BigDecimal ethBalance = Convert.fromWei(String.valueOf(ethereumCommonService.getWeb3j().ethGetBalance(refillRequestAddressDto.getAddress(), DefaultBlockParameterName.LATEST).send().getBalance()), Convert.Unit.ETHER);

                    if (balance.compareTo(ExConvert.toWei(minBalanceForTransfer, unit).toBigInteger()) <= 0){
                        refillService.updateAddressNeedTransfer(refillRequestAddressDto.getAddress(), merchant.getId(),
                                currency.getId(), false);

                        if ( ethBalance.compareTo(new BigDecimal("0.001")) > 0) {
                            Transfer.sendFunds(
                                    ethereumCommonService.getWeb3j(), credentials, ethereumCommonService.getTransferAccAddress(), ethBalance
                                            .subtract(Convert.fromWei(Transfer.GAS_LIMIT.multiply(ethereumCommonService.getWeb3j().ethGasPrice().send().getGasPrice()).toString(), Convert.Unit.ETHER)), Convert.Unit.ETHER).sendAsync();
                        }
                        continue;
                    }

                    if (ethBalance.compareTo(feeAmount) < 0) {
                        Transfer.sendFunds(
                                ethereumCommonService.getWeb3j(), ethereumCommonService.getCredentialsMain(),
                                credentials.getAddress(), feeAmount, Convert.Unit.ETHER).sendAsync();
                    }

                    contract.transfer(ethereumCommonService.getMainAddress(), balance.subtract(minWalletBalance)).send();

                    log.debug(merchantName + " Funds " + ExConvert.fromWei(String.valueOf(balance.subtract(minWalletBalance)), unit) + " sent to main account!!!");
                }

                }catch (Exception e){
                log.error(e);
            }
        }
    }

    @Override
    public TransferEventResponse extractData(List<String> topics, String data) {
        final Event event = new Event("Transfer",
                Arrays.asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Collections.singletonList(new TypeReference<Uint256>() {
                }));
        String encodedEventSignature = EventEncoder.encode(event);
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
        TransferEventResponse typedResponse = new TransferEventResponse();
        typedResponse.from = (Address) eventValues.getIndexedValues().get(0);
        typedResponse.to = (Address) eventValues.getIndexedValues().get(1);
        typedResponse.value = (Uint256) eventValues.getNonIndexedValues().get(0);
        return typedResponse;
    }

    static class TransferEventResponse {
        public Address from;

        public Address to;

        public Uint256 value;
    }

    @PreDestroy
    public void destroy() {
        log.debug("Destroying " + merchantName);
        scheduler.shutdown();
        log.debug(merchantName + " destroyed");
    }
}

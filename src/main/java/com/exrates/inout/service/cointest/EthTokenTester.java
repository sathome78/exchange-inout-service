//package com.exrates.inout.service.cointest;
//
//import com.exrates.inout.domain.dto.RefillRequestAddressDto;
//import com.exrates.inout.domain.dto.RefillRequestBtcInfoDto;
//import com.exrates.inout.domain.dto.RefillRequestCreateDto;
//import com.exrates.inout.domain.dto.RefillRequestParamsDto;
//import com.exrates.inout.domain.enums.UserRole;
//import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
//import com.exrates.inout.domain.main.CreditsOperation;
//import com.exrates.inout.domain.main.Payment;
//import com.exrates.inout.exceptions.CoinTestException;
//import com.exrates.inout.exceptions.InvalidAmountException;
//import com.exrates.inout.service.CurrencyService;
//import com.exrates.inout.service.InputOutputService;
//import com.exrates.inout.service.MerchantService;
//import com.exrates.inout.service.RefillService;
//import com.exrates.inout.service.UserService;
//import com.exrates.inout.service.ethereum.EthTokenService;
//import com.exrates.inout.service.ethereum.EthereumCommonService;
//import com.exrates.inout.service.ethereum.ethTokensWrappers.ethTokenERC20;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//import org.web3j.crypto.Credentials;
//import org.web3j.crypto.ECKeyPair;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.core.DefaultBlockParameterName;
//import org.web3j.protocol.core.methods.response.Transaction;
//import org.web3j.protocol.http.HttpService;
//
//import javax.annotation.PostConstruct;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.math.BigDecimal;
//import java.math.BigInteger;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Optional;
//
//import static com.exrates.inout.domain.enums.OperationType.INPUT;
//import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.CREATE_BY_USER;
//import static com.exrates.inout.service.cointest.BtcCoinTester.compareObjects;
//import static com.exrates.inout.service.ethereum.EthTokenServiceImpl.GAS_LIMIT;
//
//
//@Component("ethCoinTester")
//@Scope("prototype")
//public class EthTokenTester extends CoinTestBasic {
//
//    private int userId;
//    private static final int MIN_CONFIRMATION_FOR_REFILL = 1;
//    @Autowired
//    private MerchantService merchantService;
//    @Autowired
//    private InputOutputService inputOutputService;
//    @Autowired
//    private RefillService refillService;
//    @Autowired
//    private CurrencyService currencyService;
//    @Autowired
//    @Qualifier(value = "ethereumServiceImpl")
//    private EthereumCommonService ethereumCommonService;
//    @Autowired
//    private Map<String, EthTokenService> ethServicesMap;
//    @Autowired
//    private UserService userService;
//
//    private String url = "http://172.10.13.51:8549";
//
//    private int currencyId;
//    private int merchantId;
//    private String name;
//    private final static Integer TIME_FOR_REFILL = 10000;
//    private StringBuilder stringBuilder;
//    private static String mainTestAccountAddress = "0x0b958aa9601f1ca594fbe76bf879d8c29d578144";
//    private static String mainTestPrivateKey = "92562730201626919127666680751712739048456177233249322255821751422413958671494";
//    private static String mainTestPublicKey = "4170443246532098761497715728719234481946975701057637192159203344434254472506085562498955849273074226120269972199365634457382511964193135837279887855130261";
//    private static String contractTestAddress = "0x1C83501478f1320977047008496DACBD60Bb15ef";
//    //private static String name = "DGTX";
//    private ethTokenERC20 contract;
//
//    public void initBot(String name, StringBuilder stringBuilder, String email) throws Exception {
//        merchantId = merchantService.findByName(name).getId();
//        currencyId = currencyService.findByName(name).getId();
//        this.name = name;
//        this.stringBuilder = stringBuilder;
//        if(email != null) this.testEmail = email;
//        prepareContract();
//        if(email != null) this.testEmail = email;
//        stringBuilder.append("Init success for coin " + name).append("<br>");
//    }
//
//    @PostConstruct
//    public void init(){
//        userId = userService.getIdByEmail(testEmail);
//    }
//
//    private void prepareContract() throws Exception {
//
//        ECKeyPair ecKeyPair = new ECKeyPair(new BigInteger(mainTestPrivateKey), new BigInteger(mainTestPublicKey));
//
//        Credentials credentials = Credentials.create(ecKeyPair);
//
//        Web3j web3j = Web3j.build(new HttpService(url));
//
//        Class clazz = Class.forName("me.exrates.service.ethereum.ethTokensWrappers." + name);
//        Method method = clazz.getMethod("load", String.class, Web3j.class, Credentials.class, BigInteger.class, BigInteger.class);
//        BigInteger gasPrice = web3j.ethGasPrice().send().getGasPrice();
//        BigInteger gasLimitIncreased = GAS_LIMIT.multiply(new BigInteger("2"));
//        contract = (ethTokenERC20)method.invoke(null, getEthService(name).getContractAddress().get(0), web3j, credentials, gasPrice, gasLimitIncreased);
//        stringBuilder.append("Contract: " + getEthService(name).getContractAddress().get(0))
//                .append("   gasLimitIncreased = " + gasLimitIncreased)
//                .append("<br>");
//
//        stringBuilder.append("------TEST NODE INFO-----").append("Main TEST adrress = ").append(mainTestAccountAddress).append("<br>").append("Test balance ETH = ").append(web3j.ethGetBalance(mainTestAccountAddress, DefaultBlockParameterName.LATEST).send().getBalance()).append("<br>").append(name).append(" token balance = ").append(contract.balanceOf(credentials.getAddress()).send()).append("<br>");
//    }
//
//
//    @Override
//    public void testCoin(String refillAmount) throws Exception {
//        try {
//            stringBuilder.append("Starting prepareRefillRequest<br>");
//            RefillRequestCreateDto request = prepareRefillRequest(merchantId, currencyId);
//            stringBuilder.append("Set min conf<br>");
//            setMinConfirmation(MIN_CONFIRMATION_FOR_REFILL);
//            stringBuilder.append("checkNodeConnection<br>");
//            checkNodeConnection();
//            checkRefill(request, refillAmount, merchantId, currencyId);
//        } catch (Exception e){
//            stringBuilder.append(e.getMessage()).append("<br>").toString();
//        }
//    }
//
//    private void checkRefill(RefillRequestCreateDto request, String refillAmount, int merchantId, int currencyId) throws Exception {
//        stringBuilder.append("Starting check refill").append("<br>");
//        Map<String, Object> refillRequest = refillService.createRefillRequest(request);
//        String addressForRefill = (String) ((Map) refillRequest.get("params")).get("address");
//        List<RefillRequestAddressDto> byAddressMerchantAndCurrency = refillService.findByAddressMerchantAndCurrency(addressForRefill, merchantId, currencyId);
//        if (byAddressMerchantAndCurrency.size() == 0)
//            throw new CoinTestException("byAddressMerchantAndCurrency.size() == 0");
//
//        stringBuilder.append("ADDRESS FOR REFILL FROM BIRZHA " + addressForRefill).append("<br>");;
//        stringBuilder.append("addressForRefill = " + addressForRefill)
//                .append("   converted amount = " + convertToContractScale(refillAmount))
//                .append("<br>");
//        String transactionHash = contract.transfer(addressForRefill, convertToContractScale(refillAmount)).send().getTransactionHash();
//        stringBuilder.append("Transaction hash = " + transactionHash).append("<br>");
//
//
//        stringBuilder.append("Checking our transaction in explorer...");
//        BigInteger blockNumber = null;
//        do {
//            try {
//                Thread.sleep(10000);
//                Optional<Transaction> transaction = getTransactionByHash(transactionHash);
//                if (!transaction.isPresent()) {
//                    stringBuilder.append("Couldn't find tx...<br>");
//                    continue;
//                }
//                blockNumber = transaction.get().getBlockNumber();
//            } catch (Exception e){
//                stringBuilder.append(e.getMessage()).append("<br>");
//            }
//        } while (blockNumber == null);
//
//
//
//        Optional<RefillRequestBtcInfoDto> acceptedRequest;
//        do {
//            boolean isConfirmationsEnough = false;
//            acceptedRequest = refillService.findRefillRequestByAddressAndMerchantIdAndCurrencyIdAndTransactionId(merchantId, currencyId, transactionHash);
//            if (!acceptedRequest.isPresent()) {
//                if (isConfirmationsEnough) throw new RuntimeException("Confirmation enough, but refill not working!");
//                stringBuilder.append("NOT NOW(").append("<br>");;
//                Thread.sleep(2000);
//
//
//                if (ethereumCommonService.getWeb3j().ethBlockNumber().send().getBlockNumber().subtract(getTransactionByHash(transactionHash).get().getBlockNumber()).compareTo(new BigInteger(String.valueOf(MIN_CONFIRMATION_FOR_REFILL))) >= 0) {
//                    stringBuilder.append("Enough confirmation, checking refill request...");
//                    Thread.sleep(TIME_FOR_REFILL);
//                    isConfirmationsEnough = true;
//                }
//                stringBuilder.append("Checking transaction... ").append("<br>");
//            } else {
//                stringBuilder.append("accepted amount " + acceptedRequest.get().getAmount()).append("<br>");;
//                stringBuilder.append("refill amount " + refillAmount).append("<br>");;
//                RefillRequestBtcInfoDto refillRequestBtcInfoDto = acceptedRequest.get();
//                refillRequestBtcInfoDto.setAmount(new BigDecimal(refillRequestBtcInfoDto.getAmount().doubleValue()));
//
//                if (!compareObjects(refillRequestBtcInfoDto.getAmount(), (new BigDecimal(refillAmount)))) {
//                    throw new CoinTestException("!acceptedRequest.get().getAmount().equals(new BigDecimal(refillAmount)), expected " + refillAmount + " but was " + acceptedRequest.get().getAmount());
//                }
//                stringBuilder.append("REQUEST FINDED, accepted amount = " + refillRequestBtcInfoDto.getAmount()).append("<br>");;
//            }
//        } while (!acceptedRequest.isPresent());
//
//    }
//
//    private BigInteger convertToContractScale(String refillAmount) {
//        EthTokenService ethService = getEthService(name);
//        return new BigDecimal(refillAmount).multiply(new BigDecimal(10).pow(ethService.getUnit().getFactor())).toBigInteger();
//    }
//
//    private BigDecimal convertToStandartScale(BigDecimal bigDecimal) {
//        EthTokenService ethService = getEthService(name);
//        return bigDecimal.movePointLeft(ethService.getUnit().getFactor());
//    }
//
//    private Optional<Transaction> getTransactionByHash(String transactionHash) throws IOException {
//        return ethereumCommonService.getWeb3j().ethGetTransactionByHash(transactionHash).send().getTransaction();
//    }
//
//    private void checkNodeConnection() throws IOException {
//        ethereumCommonService.getWeb3j().ethGasPrice().send();
//    }
//
//    private void setMinConfirmation(int i) {
//        ethereumCommonService.setConfirmationNeededCount(1);
//    }
//
//
//    //TODO make abstract class and extract method
//    private RefillRequestCreateDto prepareRefillRequest(int merchantId, int currencyId) {
//        RefillRequestParamsDto requestParamsDto = new RefillRequestParamsDto();
//        requestParamsDto.setChildMerchant("");
//        requestParamsDto.setCurrency(currencyId);
//        requestParamsDto.setGenerateNewAddress(true);
//        requestParamsDto.setMerchant(merchantId);
//        requestParamsDto.setOperationType(INPUT);
//        requestParamsDto.setSum(null);
//
//        Payment payment = new Payment(INPUT);
//        payment.setCurrency(requestParamsDto.getCurrency());
//        payment.setMerchant(requestParamsDto.getMerchant());
//        payment.setSum(requestParamsDto.getSum() == null ? 0 : requestParamsDto.getSum().doubleValue());
//
//        Locale locale = new Locale("en");
//        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userId, UserRole.USER)
//                .orElseThrow(InvalidAmountException::new);
//        RefillStatusEnum beginStatus = (RefillStatusEnum) RefillStatusEnum.X_STATE.nextState(CREATE_BY_USER);
//
//        return new RefillRequestCreateDto(requestParamsDto, creditsOperation, beginStatus, locale);
//    }
//
//    private EthTokenService getEthService(String ticker){
//        for (Map.Entry<String, EthTokenService> entry : ethServicesMap.entrySet()) {
//            if(entry.getKey().equalsIgnoreCase(ticker + "ServiceImpl")) return entry.getValue();
//        }
//        return null;
//    }
////        String blockHash = contract.transfer("0x81fb419ACFDA6F40173b4032215101B09c4933c5", new BigInteger("1")).send().getBlockHash();
////        System.out.println(blockHash);
//}

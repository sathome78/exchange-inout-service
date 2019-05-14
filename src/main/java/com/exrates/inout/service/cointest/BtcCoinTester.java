package com.exrates.inout.service.cointest;

import com.exrates.inout.domain.dto.BtcPaymentResultDetailedDto;
import com.exrates.inout.domain.dto.BtcWalletPaymentItemDto;
import com.exrates.inout.domain.dto.MerchantCurrencyOptionsDto;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestBtcInfoDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawRequestParamsDto;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.main.WithdrawRequest;
import com.exrates.inout.exceptions.CoinTestException;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.properties.models.BitcoinCoins;
import com.exrates.inout.properties.models.BitcoinNode;
import com.exrates.inout.properties.models.BitcoinProperty;
import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.IRefillable;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.client.BtcdClientImpl;
import com.neemre.btcdcli4j.core.domain.Transaction;
import lombok.SneakyThrows;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static com.exrates.inout.domain.enums.OperationType.OUTPUT;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.CREATE_BY_USER;


@Component("btcCoinTester")
@Scope("prototype")
public class BtcCoinTester extends CoinTestBasic {

    private final static Integer TIME_FOR_REFILL = 10000;
    private static final int TEST_NODE_PORT = 8089;
    private static final String TEST_NODE_PASS = "RGGK9HsXyi3gdiEOXG8zPlyNIFq";
    private static final String TEST_NODE_USER = "devprod";
    private static final int MIN_CONFIRMATION_COUNT = 1;
    private static final double MIN_SUM_FOR_WITHDRAW = 0.00000001;


    private BtcdClient btcdClient;
    private final Object withdrawTest = new Object();
    private int withdrawStatus = 0;

    protected BtcCoinTester(String name, String email, StringBuilder stringBuilder) throws Exception {
        super(name, email, stringBuilder);
    }

    @PostConstruct
    public void setUp(){
        try {
            btcdClient = prepareTestBtcClient(name);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void testCoin(String refillAmount) {
        try {
            printTestNodeInfo();
            testAddressGeneration();
            setMinConfirmation();
            testRefill(refillAmount);
            testAutoWithdraw(refillAmount);
            stringBuilder.append("Everything works fine!<br>");
        } catch (Exception e){
            e.printStackTrace();
            stringBuilder.append(e.toString());
        }
    }

    private void printTestNodeInfo() throws BitcoindException, CommunicationException {
        stringBuilder.append("------TEST NODE INFO-----").append("<br>").append("Current balance = ").append(btcdClient.getBalance()).append("<br>").append("You can refill test node on address = ").append(btcdClient.getNewAddress()).append("<br>");
    }

    private void setMinConfirmation() {
        BitcoinService btcService = (BitcoinService) getMerchantServiceByName(name, refillableServiceMap);
        btcService.setConfirmationNeededCount(MIN_CONFIRMATION_COUNT);
    }

    private void testAddressGeneration() throws BitcoindException, CommunicationException {
        stringBuilder.append("Starting test generating address...<br>");
        stringBuilder.append("Address generation works fine. Example = ").append(btcdClient.getNewAddress()).append(" <br>");

    }

    private void testAutoWithdraw(String refillAmount) throws BitcoindException, CommunicationException, InterruptedException, CoinTestException {
        synchronized (withdrawTest) {
            String addressForWithdraw = btcdClient.getNewAddress();
            stringBuilder.append("address for withdraw ").append(addressForWithdraw).append("<br>");;

            Integer requestId = makeTestWithdraw(refillAmount, addressForWithdraw);
            WithdrawRequestFlatDto flatWithdrawRequest;

            do {
                try {
                    flatWithdrawRequest = withdrawService.getFlatById(requestId).get();
                    withdrawStatus = flatWithdrawRequest.getStatus().getCode();
                    Thread.sleep(5000);
                    if (isWithdrawProccessed()) {
                        Transaction transaction = btcdClient.getTransaction(flatWithdrawRequest.getTransactionHash());
                        if (!compareObjects(transaction.getAmount(), (flatWithdrawRequest.getAmount().subtract(flatWithdrawRequest.getCommissionAmount()))))
                            stringBuilder.append("Amount expected " + transaction.getAmount() + ", but was " + flatWithdrawRequest.getAmount().min(flatWithdrawRequest.getCommissionAmount())).append("\n");
                    }
                    stringBuilder.append("Checking withdraw...current status = ").append(withdrawStatus).append("<br>");;
                    Thread.sleep(2000);
                } catch (BitcoindException e) {
                    stringBuilder.append(e).append("<br>");;
                }
            } while (withdrawStatus != 10);

            stringBuilder.append("Auto-withdraw works").append("<br>");


        }
    }

    private boolean isWithdrawProccessed() {
        return withdrawStatus == 10;
    }

    private Integer makeTestWithdraw(String refillAmount, String addressForWithdraw) throws CoinTestException {
        WithdrawRequestParamsDto withdrawRequestParamsDto = prepareWithdrawRequestParamsDto(refillAmount, addressForWithdraw);
        Payment payment = preparePayment(withdrawRequestParamsDto);

        merchantService.setMinSum(merchantId, currencyId, MIN_SUM_FOR_WITHDRAW);
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userId, UserRole.USER)
                .orElseThrow(InvalidAmountException::new);
        WithdrawStatusEnum beginStatus = (WithdrawStatusEnum) WithdrawStatusEnum.getBeginState();

        WithdrawRequestCreateDto withdrawRequestCreateDto = new WithdrawRequestCreateDto(withdrawRequestParamsDto, creditsOperation, beginStatus);
        setAutoWithdraw(true);
        withdrawService.createWithdrawalRequest(withdrawRequestCreateDto, new Locale("en"));

        Optional<WithdrawRequest> withdrawRequestByAddressOptional = withdrawService.getWithdrawRequestByAddress(addressForWithdraw);
        if (!withdrawRequestByAddressOptional.isPresent())
            throw new CoinTestException("Empty withdrawRequestByAddressOptional");

        return withdrawRequestByAddressOptional.get().getId();
    }

    private Payment preparePayment(WithdrawRequestParamsDto withdrawRequestParamsDto) {
        Payment payment = new Payment(OUTPUT);
        payment.setCurrency(withdrawRequestParamsDto.getCurrency());
        payment.setMerchant(withdrawRequestParamsDto.getMerchant());
        payment.setSum(withdrawRequestParamsDto.getSum().doubleValue());
        payment.setDestination(withdrawRequestParamsDto.getDestination());
        payment.setDestinationTag(withdrawRequestParamsDto.getDestinationTag());
        return payment;
    }

    private WithdrawRequestParamsDto prepareWithdrawRequestParamsDto(String refillAmount, String addressForWithdraw) {
        WithdrawRequestParamsDto withdrawRequestParamsDto = new WithdrawRequestParamsDto();
        withdrawRequestParamsDto.setCurrency(currencyId);
        withdrawRequestParamsDto.setMerchant(merchantId);
        withdrawRequestParamsDto.setDestination(addressForWithdraw);
        withdrawRequestParamsDto.setDestinationTag("");
        withdrawRequestParamsDto.setOperationType(OUTPUT);
        withdrawRequestParamsDto.setSum(new BigDecimal(refillAmount));
        return withdrawRequestParamsDto;
    }

    private void testManualWithdraw(String amount) throws BitcoindException, CommunicationException, InterruptedException, CoinTestException {
        synchronized (withdrawTest) {
            setAutoWithdraw(false);

        String withdrawAddress = btcdClient.getNewAddress();
        stringBuilder.append("address for manual withdraw " + withdrawAddress).append("<br>");;
        walletPassphrase();
        BitcoinService walletService = (BitcoinService) getMerchantServiceByName(name, refillableServiceMap);
        List<BtcWalletPaymentItemDto> payments = new LinkedList<>();
        payments.add(new BtcWalletPaymentItemDto(withdrawAddress, new BigDecimal(amount)));
        BtcPaymentResultDetailedDto btcPaymentResultDetailedDto = walletService.sendToMany(payments).get(0);
        stringBuilder.append("BtcPaymentResultDetailedDto = " + btcPaymentResultDetailedDto.toString()).append("<br>");

        if(btcPaymentResultDetailedDto.getTxId() == null){
            stringBuilder.append("Cannot check manual withdraw, use walletpassphrase first!");
            return;
        }
        Transaction transaction = null;
        do {
            try {
                transaction = btcdClient.getTransaction(btcPaymentResultDetailedDto.getTxId());
            } catch (BitcoindException e) {
                stringBuilder.append("Error from btc " + e.getMessage()).append("<br>");;
            }
            Thread.sleep(2000);
            stringBuilder.append("Checking manual transaction").append("<br>");;
            if (transaction != null) {
                if (!compareObjects(btcPaymentResultDetailedDto.getAmount(), (transaction.getAmount())))
                    throw new CoinTestException("btcPaymentResultDetailedDto.getAmount() = " + btcPaymentResultDetailedDto.getAmount()
                            + " not equals with transaction.getAmount() " + transaction.getAmount());
            }
        } while (transaction == null);
        }
    }

    private void setAutoWithdraw(boolean isEnabled) {
        MerchantCurrencyOptionsDto merchantCurrencyOptionsDto = new MerchantCurrencyOptionsDto();
        merchantCurrencyOptionsDto.setCurrencyId(currencyId);
        merchantCurrencyOptionsDto.setMerchantId(merchantId);
        merchantCurrencyOptionsDto.setWithdrawAutoDelaySeconds(1);
        merchantCurrencyOptionsDto.setWithdrawAutoEnabled(isEnabled);
        merchantCurrencyOptionsDto.setWithdrawAutoThresholdAmount(new BigDecimal(999999));
        withdrawService.setAutoWithdrawParams(merchantCurrencyOptionsDto);
    }

    private void testRefill(String refillAmount) throws BitcoindException, CommunicationException, InterruptedException, CoinTestException {
        RefillRequestCreateDto request = prepareRefillRequest(merchantId, currencyId);

        Map<String, Object> refillRequest = refillService.createRefillRequest(request);
        String addressForRefill = (String) ((Map) refillRequest.get("params")).get("address");
        List<RefillRequestAddressDto> byAddressMerchantAndCurrency = refillService.findByAddressMerchantAndCurrency(addressForRefill, merchantId, currencyId);
        if (byAddressMerchantAndCurrency.size() == 0)
            throw new CoinTestException("byAddressMerchantAndCurrency.size() == 0");

        stringBuilder.append("ADDRESS FRO REFILL FROM BIRZHA ").append(addressForRefill).append("<br>");;
        stringBuilder.append("BALANCE = ").append(btcdClient.getBalance()).append("<br>");;
        walletPassphrase();
        stringBuilder.append("balance = ").append(btcdClient.getBalance()).append("<br>");;
        stringBuilder.append("refill sum = ").append(refillAmount).append("<br>");;
        stringBuilder.append("DEBUG: new BigDecimal(refillAmount)").append(new BigDecimal(refillAmount)).append("<br>");
        String txHash = btcdClient.sendToAddress(addressForRefill, new BigDecimal(refillAmount));

        Optional<RefillRequestBtcInfoDto> acceptedRequest;
        Integer minConfirmation = getMerchantServiceByName(name, refillableServiceMap).minConfirmationsRefill();

        do {
            acceptedRequest = refillService.findRefillRequestByAddressAndMerchantIdAndCurrencyIdAndTransactionId(merchantId, currencyId, txHash);
            if (!acceptedRequest.isPresent()) {
                refillRequestJob.forceCheckPaymentsForCoin(name);
                Thread.sleep(2000);
                Transaction transaction = btcdClient.getTransaction(txHash);
                if (transaction.getConfirmations() >= minConfirmation) {
                    Thread.sleep(TIME_FOR_REFILL);
                }
                String trxConf = "Transaction consfirmation = ";
                stringBuilder.append(trxConf).append(transaction.getConfirmations()).append("<br>");;
            } else {
                stringBuilder.append("accepted amount ").append(acceptedRequest.get().getAmount()).append("<br>");;
                stringBuilder.append("refill amount ").append(refillAmount).append("<br>");;
                RefillRequestBtcInfoDto refillRequestBtcInfoDto = acceptedRequest.get();
                refillRequestBtcInfoDto.setAmount(new BigDecimal(refillRequestBtcInfoDto.getAmount().doubleValue()));
            }
        } while (!acceptedRequest.isPresent());

        stringBuilder.append("THE REQUEST WAS FOUND").append("<br>");;
        stringBuilder.append("Node balance after refill = " + btcdClient.getBalance()).append("<br>");
        Map<String, String> a = new HashMap<>();
    }

    private void walletPassphrase() {
        try {
            btcdClient.walletPassphrase(TEST_NODE_PASS, 2000);
        } catch (Exception e) {
            stringBuilder.append("Error while trying encrypt wallet: <br>").append(e.getMessage()).append("<br>");;
        }
    }

    private RefillRequestCreateDto prepareRefillRequest(int merchantId, int currencyId) {
        RefillRequestParamsDto requestParamsDto = new RefillRequestParamsDto();
        requestParamsDto.setChildMerchant("");
        requestParamsDto.setCurrency(currencyId);
        requestParamsDto.setGenerateNewAddress(true);
        requestParamsDto.setMerchant(merchantId);
        requestParamsDto.setOperationType(INPUT);
        requestParamsDto.setSum(null);

        Payment payment = new Payment(INPUT);
        payment.setCurrency(requestParamsDto.getCurrency());
        payment.setMerchant(requestParamsDto.getMerchant());
        payment.setSum(requestParamsDto.getSum() == null ? 0 : requestParamsDto.getSum().doubleValue());

        Locale locale = new Locale("en");
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userId, UserRole.USER)
                .orElseThrow(InvalidAmountException::new);
        RefillStatusEnum beginStatus = (RefillStatusEnum) RefillStatusEnum.X_STATE.nextState(CREATE_BY_USER);

        return new RefillRequestCreateDto(requestParamsDto, creditsOperation, beginStatus, locale);
    }

    @SneakyThrows
    private BtcdClient prepareTestBtcClient(String name) throws Exception {
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        CloseableHttpClient httpProvider = HttpClients.custom().setConnectionManager(cm)
                .build();

        BitcoinCoins bitcoinCoins = ccp.getBitcoinCoins();
        Field field = BitcoinCoins.class.getDeclaredField(name.toLowerCase());
        field.setAccessible(true);
        BitcoinNode bitcoinProperty = ((BitcoinProperty)field.get(bitcoinCoins)).getNode();

        return new BtcdClientImpl(httpProvider,
                bitcoinProperty.getRpcProtocol(),
                bitcoinProperty.getRpcHost(),
                TEST_NODE_PORT,
                TEST_NODE_USER,
                TEST_NODE_PASS
                );
    }

    private IRefillable getMerchantServiceByName(String name, Map<String, IRefillable> merchantServiceMap) {
        for (Map.Entry<String, IRefillable> e : merchantServiceMap.entrySet()) {
            if (e.getValue().getMerchantName().equals(name)) return e.getValue();
        }
        throw new RuntimeException("BitcoinService with ticker " + name + " not found!");
    }


    public static boolean compareObjects(Object A, Object B) {
        return normalize(A).equals(normalize(B));
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal("0.0001").multiply(new BigDecimal("0.00010")));
    }

    private static String normalize(Object B) {
        BigDecimal A = new BigDecimal(String.valueOf(B));
        StringBuilder first = new StringBuilder(String.valueOf(A));
        String check = String.valueOf(A);
        if (!check.contains(".")) return check;

        String substring = "";
        substring = check.substring(check.indexOf(".") + 1);

        if (substring.length() > 8) {
            first = new StringBuilder(substring.substring(0, 8));
        } else first = new StringBuilder(substring.substring(0, substring.length()));


        for (int i = first.length() - 1; i != -1; i--) {
            if (String.valueOf(first.charAt(i)).equals("0")) {
                first.deleteCharAt(i);
            } else break;
        }
        String result = check.substring(0, check.indexOf(".") + 1) + first.toString();
        return result;
    }


}

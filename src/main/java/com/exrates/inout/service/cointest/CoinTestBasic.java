package com.exrates.inout.service.cointest;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestParamsDto;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.exceptions.InvalidAmountException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CoinTester;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.InputOutputService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.WithdrawService;
import com.exrates.inout.service.job.invoice.RefillRequestJob;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.CREATE_BY_USER;

public abstract class CoinTestBasic implements CoinTester {

    protected String testEmail = "yagi3773@gmail.com";
    protected int userId;
    protected int currencyId;
    protected int merchantId;
    protected String name;
    @Autowired
    protected UserService userService;
    @Autowired
    protected Map<String, IRefillable> refillableServiceMap;
    @Autowired
    protected MerchantService merchantService;
    @Autowired
    protected InputOutputService inputOutputService;
    @Autowired
    protected RefillService refillService;
    @Autowired
    protected CurrencyService currencyService;
    @Autowired
    protected WithdrawService withdrawService;
    @Autowired
    protected RefillRequestJob refillRequestJob;
    @Autowired
    protected CryptoCurrencyProperties ccp;
    protected StringBuilder stringBuilder;

    protected CoinTestBasic(String name, String email, StringBuilder stringBuilder){
        this.name = name;
        this.stringBuilder = stringBuilder;
        if(email != null) this.testEmail = email;
        stringBuilder.append("Init success for coin " + name).append("<br>");

    }

    @PostConstruct
    public void init(){
        merchantId = merchantService.findByName(name).getId();
        currencyId = currencyService.findByName(name).getId();
        userId = userService.getIdByEmail(testEmail);
    }

    protected RefillRequestCreateDto prepareRefillRequest(int merchantId, int currencyId) {
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

    public static boolean compareObjects(Object A, Object B) {
        return normalize(A).equals(normalize(B));
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
        } else first = new StringBuilder(substring);


        for (int i = first.length() - 1; i != -1; i--) {
            if (String.valueOf(first.charAt(i)).equals("0")) {
                first.deleteCharAt(i);
            } else break;
        }
        String result = check.substring(0, check.indexOf(".") + 1) + first.toString();
        return result;
    }

}

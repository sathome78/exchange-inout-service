package com.exrates.inout;

import com.exrates.inout.dao.UserDao;
import com.exrates.inout.domain.enums.UserStatus;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.dto.TestUser;
import com.exrates.inout.service.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.HashMap;

@SpringBootTest(classes = {InoutApplication.class, InoutTestConfig.class})
@RunWith(SpringRunner.class)
public class InoutTestApplication {

    private static int id = 0;

    @Autowired
    protected UserDao userDao;
    @Autowired
    protected MerchantService merchantService;
    @Autowired
    protected CurrencyService currencyService;

    @Autowired
    @Qualifier("masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @MockBean
    protected WalletService walletService;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    protected CompanyWalletService companyWalletService;

    @MockBean
    private GtagService gtagService;

    @MockBean
    protected SecureService secureService;

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;

    protected Merchant aunitMerchant;
    protected Currency aunitCurrency;

    @Before
    public void clean(){
        jdbcTemplate.update("DELETE FROM WITHDRAW_REQUEST WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_CONFIRMATION WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_ADDRESS WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM USER WHERE 1", new HashMap<>());

        aunitMerchant = merchantService.findByName("AUNIT");
        aunitCurrency = currencyService.findByName("AUNIT");

    }

    protected TestUser registerNewUser(){
        User user = new User();
        user.setEmail("user" + id++ +"@gmail.com");
        user.setStatus(UserStatus.ACTIVE);
        userDao.create(user);
        user.setId(userDao.getIdByEmail(user.getEmail()));

        return new TestUser(user.getId(), user.getEmail());
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
        if(String.valueOf(result.charAt(result.length() - 1)).equals(".")) return result.replace(".", "");
        return result;
    }
}

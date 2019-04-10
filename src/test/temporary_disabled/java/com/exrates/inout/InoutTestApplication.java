package com.exrates.inout;

import com.exrates.inout.configuration.RabbitConfig;
import com.exrates.inout.controller.interceptor.TokenInterceptor;
import com.exrates.inout.dao.UserDao;
import com.exrates.inout.domain.enums.UserStatus;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.NotificationService;
import com.exrates.inout.service.WalletService;
import com.exrates.inout.service.impl.RabbitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

@SpringBootTest(classes = {InoutApplication.class, InoutTestConfig.class})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class InoutTestApplication {

    private static int id = 0;

    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected UserDao userDao;
    @Autowired
    protected MerchantService merchantService;
    @Autowired
    protected CurrencyService currencyService;
    @MockBean
    protected TokenInterceptor tokenInterceptor;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    @Qualifier("masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @MockBean
    protected WalletService walletService;

    @MockBean
    protected NotificationService notificationService;

    @MockBean
    private GtagService gtagService;

    @MockBean
    protected RabbitService rabbitService;

    @MockBean
    private RabbitConfig rabbitConfig;

    @SpyBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private RabbitAdmin rabbitAdmin;

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;

    protected Merchant aunitMerchant;
    protected Currency aunitCurrency;

    @Before
    public void clean(){
        System.out.println("clean");
        jdbcTemplate.update("DELETE FROM WITHDRAW_REQUEST WHERE 1", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_CONFIRMATION WHERE 1", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST WHERE 1", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_ADDRESS WHERE 1", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM USER_COMMENT WHERE 1", Collections.emptyMap());
        jdbcTemplate.update("DELETE FROM USER WHERE 1", Collections.emptyMap());

        aunitMerchant = merchantService.findByName("AUNIT");
        aunitCurrency = currencyService.findByName("AUNIT");

        Mockito.when(tokenInterceptor.getAUTH_TOKEN_NAME()).thenReturn("mock");
        Mockito.when(tokenInterceptor.getAUTH_TOKEN_VALUE()).thenReturn("mock");
    }

    protected User registerNewUser(){
        User user = new User();
        user.setEmail("user" + id++ +"@gmail.com");
        user.setStatus(UserStatus.ACTIVE);
        user.setId(id);
        userDao.create(user);

        return new User(user.getId(), user.getEmail());
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

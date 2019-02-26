package com.exrates.inout;

import com.exrates.inout.dao.UserDao;
import com.exrates.inout.domain.enums.UserOperationAuthority;
import com.exrates.inout.domain.enums.UserStatus;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.dto.UserInfoDto;
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

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(classes = {InoutApplication.class, InoutTestConfig.class})
@RunWith(SpringRunner.class)
public class InoutTestApplication {

    private static final int ENABLED = 1;
    private static int id = 0;

    @Autowired
    private UserDao userDao;

    @Autowired
    @Qualifier("masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private CurrencyService currencyService;

    @MockBean
    private WalletService walletService;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private CompanyWalletService companyWalletService;

    @MockBean
    private GtagService gtagService;

    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;


    @Before
    public void clean(){
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_CONFIRMATION WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM REFILL_REQUEST_ADDRESS WHERE 1", new HashMap<>());
        jdbcTemplate.update("DELETE FROM USER WHERE 1", new HashMap<>());

    }

    protected UserInfoDto registerNewUser(){
        User user = new User();
        user.setEmail("user" + id++ +"@gmail.com");
        user.setStatus(UserStatus.ACTIVE);
        userDao.create(user);
        user.setId(userDao.getIdByEmail(user.getEmail()));
//        insertAuthorities(user);

        return new UserInfoDto(user.getId(), user.getEmail());
    }

    private void insertAuthorities(User user){
        String sql = "INSERT INTO USER_OPERATION_AUTHORITY (user_id, user_operation_id, enabled) " +
                "VALUES (:userId, :user_operation_id, :enabled);";

        for (UserOperationAuthority value : UserOperationAuthority.values()) {
            Map<String, Integer> params = new HashMap<>();
            params.put("userId", user.getId());
            params.put("user_operation_id", value.operationId);
            params.put("enabled", ENABLED);

            jdbcTemplate.update(sql, params);
        }

    }
}

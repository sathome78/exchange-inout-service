package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.UserDao;
import com.exrates.inout.domain.dto.UpdateUserDto;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.TokenType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.UserStatus;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.main.Comment;
import com.exrates.inout.domain.main.TemporalToken;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.exceptions.UserNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);


    private final String SELECT_USER =
            "SELECT USER.id, u.email AS parent_email, USER.finpassword, USER.nickname, USER.email, USER.password, USER.regdate, " +
                    "USER.phone, USER.status, USER.kyc_status, USER_ROLE.name AS role_name, USER.country AS country, USER.pub_id FROM USER " +
                    "INNER JOIN USER_ROLE ON USER.roleid = USER_ROLE.id LEFT JOIN REFERRAL_USER_GRAPH " +
                    "ON USER.id = REFERRAL_USER_GRAPH.child LEFT JOIN USER AS u ON REFERRAL_USER_GRAPH.parent = u.id ";

    private final String SELECT_COUNT = "SELECT COUNT(*) FROM USER " +
            "INNER JOIN USER_ROLE ON USER.roleid = USER_ROLE.id LEFT JOIN REFERRAL_USER_GRAPH " +
            "ON USER.id = REFERRAL_USER_GRAPH.child LEFT JOIN USER AS u ON REFERRAL_USER_GRAPH.parent = u.id ";

    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<User> getUserRowMapper() {
        return (resultSet, i) -> {
            final User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setNickname(resultSet.getString("nickname"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));
            user.setRegdate(resultSet.getDate("regdate"));
            user.setPhone(resultSet.getString("phone"));
            user.setUserStatus(UserStatus.values()[resultSet.getInt("status") - 1]);
            user.setRole(UserRole.valueOf(resultSet.getString("role_name")));
            user.setFinpassword(resultSet.getString("finpassword"));
            user.setKycStatus(resultSet.getString("kyc_status"));
            user.setCountry(resultSet.getString("country"));
            user.setPublicId(resultSet.getString("pub_id"));
            try {
                user.setParentEmail(resultSet.getString("parent_email")); // May not exist for some users
            } catch (final SQLException e) {/*NOP*/}
            return user;
        };
    }

    public int getIdByEmail(String email) {
        String sql = "SELECT id FROM USER WHERE email = :email";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("email", email);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public int getIdByNickname(String nickname) {
        String sql = "SELECT id FROM USER WHERE nickname = :nickname";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("nickname", nickname);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public boolean create(User user) {
        String sqlUser = "insert into USER(id,nickname,email,password,phone,status,roleid ) " +
                "values(:id,:nickname,:email,:password,:phone,:status,:roleid)";
        String sqlWallet = "INSERT INTO WALLET (currency_id, user_id) select id, :user_id from CURRENCY;";
        String sqlNotificationOptions = "INSERT INTO NOTIFICATION_OPTIONS(notification_event_id, user_id, send_notification, send_email) " +
                "select id, :user_id, default_send_notification, default_send_email FROM NOTIFICATION_EVENT; ";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("email", user.getEmail());
        namedParameters.put("id", user.getId());
        namedParameters.put("nickname", user.getNickname());
        if (user.getPassword() != null) {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(user.getPassword());
            namedParameters.put("password", hashedPassword);
        } else {
            namedParameters.put("password", user.getPassword());
        }
        String phone = user.getPhone();
        if (user.getPhone() != null && user.getPhone().equals("")) {
            phone = null;
        }
        namedParameters.put("phone", phone);
        namedParameters.put("status", String.valueOf(user.getUserStatus().getStatus()));
        namedParameters.put("roleid", String.valueOf(user.getRole().getRole()));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        return namedParameterJdbcTemplate.update(sqlUser, new MapSqlParameterSource(namedParameters), keyHolder) == 1;
    }

    public UserRole getUserRoleById(Integer id) {
        String sql = "select USER_ROLE.name as role_name from USER " +
                "inner join USER_ROLE on USER.roleid = USER_ROLE.id where USER.id = :id ";
        Map<String, Integer> namedParameters = Collections.singletonMap("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, (rs, row) -> UserRole.valueOf(rs.getString("role_name")));
    }

    public User findByEmail(String email) {
        String sql = SELECT_USER + "WHERE USER.email = :email";
        final Map<String, String> params = new HashMap<String, String>() {
            {
                put("email", email);
            }
        };
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, getUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("email: %s", email));
        }
    }

    public User findByNickname(String nickname) {
        String sql = SELECT_USER + "WHERE USER.nickname = :nickname";
        final Map<String, String> params = new HashMap<String, String>() {
            {
                put("nickname", nickname);
            }
        };
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, getUserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new UserNotFoundException(String.format("nickname: %s", nickname));
        }
    }

    public User getUserById(int id) {
        String sql = SELECT_USER + "WHERE USER.id = :id";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("id", String.valueOf(id));
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, getUserRowMapper());
    }

    public boolean ifNicknameIsUnique(String nickname) {
        String sql = "SELECT id FROM USER WHERE nickname = :nickname";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("nickname", nickname);
        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, row) -> {
            if (rs.next()) {
                return rs.getInt("id");
            } else return 0;
        }).isEmpty();
    }

    public boolean ifEmailIsUnique(String email) {
        String sql = "SELECT id FROM USER WHERE email = :email";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("email", email);
        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, row) -> {
            if (rs.next()) {
                return rs.getInt("id");
            } else return 0;
        }).isEmpty();
    }

    public boolean update(UpdateUserDto user) {
        throw new NotImplementedException();

    }

    public boolean createTemporalToken(TemporalToken token) {
        String sql = "insert into TEMPORAL_TOKEN(value,user_id,token_type_id,check_ip) values(:value,:user_id,:token_type_id,:check_ip)";
        Map<String, String> namedParameters = new HashMap<String, String>();
        namedParameters.put("value", token.getValue());
        namedParameters.put("user_id", String.valueOf(token.getUserId()));
        namedParameters.put("token_type_id", String.valueOf(token.getTokenType().getTokenType()));
        namedParameters.put("check_ip", token.getCheckIp());
        return namedParameterJdbcTemplate.update(sql, namedParameters) > 0;
    }

    public TemporalToken verifyToken(String token) {
        String sql = "SELECT * FROM TEMPORAL_TOKEN WHERE VALUE= :value";
        Map<String, String> namedParameters = new HashMap<String, String>();
        namedParameters.put("value", token);
        ArrayList<TemporalToken> result = (ArrayList<TemporalToken>) namedParameterJdbcTemplate.query(sql, namedParameters, new BeanPropertyRowMapper<TemporalToken>() {
            @Override
            public TemporalToken mapRow(ResultSet rs, int rowNumber) throws SQLException {
                TemporalToken temporalToken = new TemporalToken();
                temporalToken.setId(rs.getInt("id"));
                temporalToken.setUserId(rs.getInt("user_id"));
                temporalToken.setValue(token);
                temporalToken.setAlreadyUsed(rs.getBoolean("already_used"));
                temporalToken.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                temporalToken.setExpired(rs.getBoolean("expired"));
                temporalToken.setTokenType(TokenType.convert(rs.getInt("token_type_id")));
                temporalToken.setCheckIp(rs.getString("check_ip"));
                return temporalToken;
            }
        });
        return result.size() == 1 ? result.get(0) : null;
    }

    public List<TemporalToken> getAllTokens() {
        String sql = "SELECT * FROM TEMPORAL_TOKEN";
        ArrayList<TemporalToken> result = (ArrayList<TemporalToken>) namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<TemporalToken>() {
            @Override
            public TemporalToken mapRow(ResultSet rs, int rowNumber) throws SQLException {
                TemporalToken temporalToken = new TemporalToken();
                temporalToken.setId(rs.getInt("id"));
                temporalToken.setUserId(rs.getInt("user_id"));
                temporalToken.setValue(rs.getString("value"));
                temporalToken.setDateCreation(rs.getTimestamp("date_creation").toLocalDateTime());
                temporalToken.setExpired(rs.getBoolean("expired"));
                temporalToken.setTokenType(TokenType.convert(rs.getInt("token_type_id")));
                temporalToken.setCheckIp(rs.getString("check_ip"));
                return temporalToken;
            }
        });
        return result;
    }

    public String getPreferredLang(int userId) {
        String sql = "SELECT preferred_lang FROM USER WHERE id = :id";
        Map<String, Integer> namedParameters = new HashMap<>();
        namedParameters.put("id", userId);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public String getPreferredLangByEmail(String email) {
        String sql = "SELECT preferred_lang FROM USER WHERE email = :email";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("email", email);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, String.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean insertIp(String email, String ip) {
        String sql = "INSERT INTO USER_IP (user_id, ip)" +
                " SELECT id, '" + ip + "'" +
                " FROM USER " +
                " WHERE USER.email = :email";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("email", email);
        return namedParameterJdbcTemplate.update(sql, namedParameters) > 0;
    }

    public Long saveTemporaryPassword(Integer userId, String password, Integer tokenId) {
        String sql = "INSERT INTO API_TEMP_PASSWORD(user_id, password, date_creation, temporal_token_id) VALUES (:userId, :password, NOW(), :tokenId);";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("userId", userId);
        namedParameters.put("password", encodedPassword);
        namedParameters.put("tokenId", tokenId);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(namedParameters), keyHolder);
        return (Long) keyHolder.getKey();
    }

    public boolean addUserComment(Comment comment) {
        String sql = "INSERT INTO USER_COMMENT (user_id, users_comment, user_creator_id, message_sent, topic_id) " +
                "VALUES (:user_id, :comment, :user_creator_id, :message_sent, :topic_id);";
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("user_id", comment.getUser().getId());
        namedParameters.put("comment", comment.getComment());
        namedParameters.put("user_creator_id", comment.getCreator() == null ? -1 : comment.getCreator().getId());
        namedParameters.put("message_sent", comment.isMessageSent());
        namedParameters.put("topic_id", comment.getUserCommentTopic().getCode());
        return namedParameterJdbcTemplate.update(sql, namedParameters) > 0;
    }

    public InvoiceOperationPermission getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(
            Integer userId,
            Integer currencyId,
            InvoiceOperationDirection invoiceOperationDirection) {
        String sql = "SELECT invoice_operation_permission_id " +
                " FROM USER_CURRENCY_INVOICE_OPERATION_PERMISSION " +
                " WHERE user_id = :user_id AND currency_id = :currency_id AND operation_direction = :operation_direction";
        Map<String, Object> params = new HashMap<String, Object>() {{
            put("user_id", userId);
            put("currency_id", currencyId);
            put("operation_direction", invoiceOperationDirection.name());
        }};
        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, idx) ->
                InvoiceOperationPermission.convert(rs.getInt("invoice_operation_permission_id")));
    }

    public String getEmailById(Integer id) {
        String sql = "SELECT email FROM USER WHERE id = :id";
        return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), String.class);
    }

    public String getPinByEmailAndEvent(String email, NotificationMessageEventEnum event) {
        final String sql = String.format("SELECT %s_pin FROM USER " +
                " WHERE email = :email ", event.name().toLowerCase());
        return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("email", email), String.class);
    }

    public void updatePinByUserEmail(String userEmail, String pin, NotificationMessageEventEnum event) {
        String sql = String.format("UPDATE USER SET %s_pin = :pin " +
                "WHERE USER.email = :email", event.name().toLowerCase());
        Map<String, Object> namedParameters = new HashMap<String, Object>() {{
            put("email", userEmail);
            put("pin", pin);
        }};
        namedParameterJdbcTemplate.update(sql, namedParameters);
    }

    @Override
    public User getCommonReferralRoot() {
        final String sql = "SELECT USER.id, nickname, email, password, finpassword, regdate, phone, status, USER_ROLE.name as role_name FROM COMMON_REFERRAL_ROOT INNER JOIN USER ON COMMON_REFERRAL_ROOT.user_id = USER.id INNER JOIN USER_ROLE ON USER.roleid = USER_ROLE.id LIMIT 1";
        final List<User> result = namedParameterJdbcTemplate.query(sql, getUserRowMapper());
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    public UserRole getUserRoles(String email) {
        String sql = "select USER_ROLE.name as role_name from USER " +
                "inner join USER_ROLE on USER.roleid = USER_ROLE.id where USER.email = :email ";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("email", email);
        return namedParameterJdbcTemplate.query(sql, namedParameters, (rs, row) -> {
            UserRole role = UserRole.valueOf(rs.getString("role_name"));
            return role;
        }).get(0);
    }

}

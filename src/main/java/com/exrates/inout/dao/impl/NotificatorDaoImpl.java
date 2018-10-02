package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.NotificatorsDao;
import com.exrates.inout.domain.dto.Notificator;
import com.exrates.inout.domain.enums.NotificationPayTypeEnum;
import com.exrates.inout.domain.enums.NotificationTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class NotificatorDaoImpl implements NotificatorsDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    private static RowMapper<Notificator> notificatorRowMapper = (rs, idx) -> {
        Notificator notificator = new Notificator();
        notificator.setId(rs.getInt("id"));
        notificator.setBeanName(rs.getString("bean_name"));
        notificator.setPayTypeEnum(NotificationPayTypeEnum.valueOf(rs.getString("pay_type")));
        notificator.setName(rs.getString("name"));
        notificator.setEnabled(rs.getBoolean("enable"));
        notificator.setNeedSubscribe(NotificationTypeEnum.convert(notificator.getId()).isNeedSubscribe());
        return notificator;
    };

    @Autowired
    public NotificatorDaoImpl(@Qualifier(value = "masterTemplate") NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Notificator getById(int id) {
        String sql = "SELECT * FROM 2FA_NOTIFICATOR WHERE id = :id ";
        Map<String, Object> params = new HashMap<>() {{
            put("id", id);
        }};
        return jdbcTemplate.queryForObject(sql, params, notificatorRowMapper);
    }

    public List<Notificator> getAllNotificators() {
        String sql = "SELECT * FROM 2FA_NOTIFICATOR";
        return jdbcTemplate.query(sql, notificatorRowMapper);
    }
}

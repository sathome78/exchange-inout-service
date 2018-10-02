package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.NotificationDao;
import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.NotificationOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class NotificationDaoImpl implements NotificationDao {

    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final RowMapper<NotificationOption> notificationOptionRowMapper = (resultSet, row) -> {
        NotificationOption option = new NotificationOption();
        option.setEvent(NotificationEvent.convert(resultSet.getInt("notification_event_id")));
        option.setUserId(resultSet.getInt("user_id"));
        option.setSendNotification(resultSet.getBoolean("send_notification"));
        option.setSendEmail(resultSet.getBoolean("send_email"));
        return option;
    };

    public NotificationOption findUserOptionForEvent(Integer userId, NotificationEvent event) {
        String sql = "SELECT notification_event_id, user_id, send_notification, send_email " +
                " FROM NOTIFICATION_OPTIONS " +
                " WHERE user_id = :user_id AND notification_event_id = :notification_event_id";
        Map<String, Integer> params = new HashMap<>() {{
            put("user_id", userId);
            put("notification_event_id", event.getEventType());
        }};
        return jdbcTemplate.queryForObject(sql, params, notificationOptionRowMapper);

    }
}

package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.Notificator;

import java.util.List;

public interface NotificatorsDao {
    Notificator getById(int id);

    List<Notificator> getAllNotificators();
}

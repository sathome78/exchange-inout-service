package com.exrates.inout.service;

import com.exrates.inout.domain.ReferralLevel;

import java.math.BigDecimal;
import java.util.List;

public interface ReferralLevelDao {

    List<ReferralLevel> findAll();

    ReferralLevel findById(int id);

    BigDecimal getTotalLevelsPercent();

    int create(ReferralLevel level);

    void delete(int levelId);
}

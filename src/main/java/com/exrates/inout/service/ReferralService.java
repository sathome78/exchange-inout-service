package com.exrates.inout.service;

import com.exrates.inout.domain.ReferralLevel;
import com.exrates.inout.domain.ReferralTransaction;
import com.exrates.inout.domain.dto.MyReferralDetailedDto;
import com.exrates.inout.domain.dto.RefFilterData;
import com.exrates.inout.domain.dto.RefsListContainer;
import com.exrates.inout.domain.enums.ReferralTransactionStatusEnum;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.ExOrder;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.util.CacheData;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface ReferralService {

    String generateReferral(String userEmail);

    void processReferral(ExOrder exOrder, final BigDecimal commissionAmount, Currency currency, int userId);

    List<ReferralTransaction> findAll(int userId);

    Integer getReferralParentId(int childId);

    @Transactional
    void setRefTransactionStatus(ReferralTransactionStatusEnum status, int refTransactionId);
}

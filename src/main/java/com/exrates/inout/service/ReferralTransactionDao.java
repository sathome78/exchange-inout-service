package com.exrates.inout.service;

import com.exrates.inout.domain.ReferralTransaction;
import com.exrates.inout.domain.dto.MyReferralDetailedDto;
import com.exrates.inout.domain.enums.ReferralTransactionStatusEnum;

import java.util.List;
import java.util.Locale;

public interface ReferralTransactionDao {

    List<ReferralTransaction> findAll(int userId);

    List<ReferralTransaction> findAll(int userId, int offset, int limit);

    ReferralTransaction create(ReferralTransaction referralTransaction);

    List<MyReferralDetailedDto> findAllMyRefferal(String email, Integer offset, Integer limit, Locale locale);

    void setRefTransactionStatus(ReferralTransactionStatusEnum status, int refTransactionId);
}

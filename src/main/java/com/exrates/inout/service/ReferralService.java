package com.exrates.inout.service;

import com.exrates.inout.domain.ReferralTransaction;

import java.util.List;

public interface ReferralService {

    String generateReferral(String userEmail);

    List<ReferralTransaction> findAll(int userId);

    Integer getReferralParentId(int childId);
}

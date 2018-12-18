package com.exrates.inout.service.impl;

import com.exrates.inout.dao.ReferralTransactionDao;
import com.exrates.inout.dao.ReferralUserGraphDao;
import com.exrates.inout.domain.ReferralTransaction;
import com.exrates.inout.domain.dto.RefFilterData;
import com.exrates.inout.domain.dto.ReferralInfoDto;
import com.exrates.inout.domain.dto.ReferralProfitDto;
import com.exrates.inout.service.CommissionService;
import com.exrates.inout.service.ReferralService;
import com.exrates.inout.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Log4j2
@Service
public class ReferralServiceImpl implements ReferralService {

    @Autowired
    private ReferralUserGraphDao referralUserGraphDao;
    @Autowired
    private ReferralTransactionDao referralTransactionDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommissionService commissionService;

    /**
     * URL following format  - xxx/register?ref=
     * where xxx is replaced by the domain name depending on the maven profile
     */
    private
    @Value("${referral.url}")
    String referralUrl;

    /**
     * Generates referral reference following format : [3 random digits] Sponsor UserId [3 random digits]
     *
     * @param userEmail Sponsor email
     * @return String contains referral reference
     */
    @Override
    public String generateReferral(final String userEmail) {
        final int userId = userService.getIdByEmail(userEmail);
        int prefix = new Random().nextInt(999 - 100 + 1) + 100;
        int suffix = new Random().nextInt(999 - 100 + 1) + 100;
        return referralUrl + prefix + userId + suffix;
    }

    @Override
    public List<ReferralTransaction> findAll(final int userId) {
        return referralTransactionDao.findAll(userId);
    }

    @Override
    public Integer getReferralParentId(int childId) {
        return referralUserGraphDao.getParent(childId);
    }

    private void setDetailedAmountToDtos(List<ReferralInfoDto> list, int profitUser, RefFilterData refFilterData) {
        list.stream().filter(p -> p.getRefProfitFromUser() > 0)
                .forEach(l -> l.setReferralProfitDtoList(referralUserGraphDao.detailedCountRefsTransactions(l.getRefId(), profitUser, refFilterData)));
    }

    private String refProfitString(List<ReferralProfitDto> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(i -> {
            sb.append(i.getAmount());
            sb.append(i.getCurrencyName());
            sb.append(", ");
        });
        sb.deleteCharAt(sb.lastIndexOf(","));
        return sb.toString();
    }
}

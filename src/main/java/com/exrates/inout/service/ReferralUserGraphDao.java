package com.exrates.inout.service;

import com.exrates.inout.domain.dto.RefFilterData;
import com.exrates.inout.domain.dto.ReferralInfoDto;
import com.exrates.inout.domain.dto.ReferralProfitDto;
import java.util.List;

public interface ReferralUserGraphDao {

    void create(int child, int parent);

    Integer getParent(Integer child);
    
    List<Integer> getChildrenForParentAndBlock(Integer parent);
    
    void changeReferralParent(Integer formerParent, Integer newParent);

    List<ReferralInfoDto> getInfoAboutFirstLevRefs(int userId, int profitUser, int limit, int offset, RefFilterData refFilterData);

    ReferralInfoDto getInfoAboutUserRef(int userId, int profitUser, RefFilterData filterData);

    List<ReferralProfitDto> detailedCountRefsTransactions(Integer userId, int profitUser, RefFilterData refFilterData);

    int getInfoAboutFirstLevRefsTotalSize(int parentId);
}

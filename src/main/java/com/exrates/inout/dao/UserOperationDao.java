package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.UserOperationAuthorityOption;
import com.exrates.inout.domain.enums.UserOperationAuthority;

import java.util.List;

public interface UserOperationDao {

    boolean getStatusAuthorityForUserByOperation(int userId, UserOperationAuthority userOperationAuthority);

    List<UserOperationAuthorityOption> getUserOperationAuthorityOption(Integer userId);

    void updateUserOperationAuthority(List<UserOperationAuthorityOption> options, Integer userId);


}
package com.exrates.inout.service;

import com.exrates.inout.domain.enums.BusinessUserRoleEnum;
import com.exrates.inout.domain.enums.GroupUserRoleEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.UserRoleSettings;

import java.util.List;

public interface UserRoleService {

    List<UserRole> getRealUserRoleByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum);

    List<Integer> getRealUserRoleIdByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum);

    List<UserRole> getRealUserRoleByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum);

    List<Integer> getRealUserRoleIdByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum);

    boolean isOrderAcceptionAllowedForUser(Integer userId);

    UserRoleSettings retrieveSettingsForRole(Integer roleId);
}

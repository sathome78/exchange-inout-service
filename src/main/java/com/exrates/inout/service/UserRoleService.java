package com.exrates.inout.service;

import com.exrates.inout.domain.enums.BusinessUserRoleEnum;
import com.exrates.inout.domain.enums.GroupUserRoleEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.UserRoleSettings;

import java.util.List;

public interface UserRoleService {

  List<UserRole> getRealUserRoleByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum);

  List<Integer> getRealUserRoleIdByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum);

  String[] getRealUserRoleNameByBusinessRoleArray(BusinessUserRoleEnum businessUserRoleEnum);

  List<Integer> getRealUserRoleIdByBusinessRoleList(String businessUserRoleName);

  List<UserRole> getRealUserRoleByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum);

  List<Integer> getRealUserRoleIdByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum);

  List<Integer> getRealUserRoleIdByGroupRoleList(String groupUserRoleName);

  boolean isOrderAcceptionAllowedForUser(Integer userId);

    UserRoleSettings retrieveSettingsForRole(Integer roleId);

    List<UserRole> getRolesAvailableForChangeByAdmin();

    List<UserRoleSettings> retrieveSettingsForAllRoles();

    void updateSettingsForRole(UserRoleSettings settings);

    List<UserRole> getRolesConsideredForPriceRangeComputation();

  List<UserRole> getRolesUsingRealMoney();
}

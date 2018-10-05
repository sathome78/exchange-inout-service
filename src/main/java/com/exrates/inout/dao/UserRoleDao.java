package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.UserRoleSettings;

import java.util.List;

public interface UserRoleDao {
  List<UserRole> findRealUserRoleIdByBusinessRoleList(String businessRoleName);

  List<UserRole> findRealUserRoleIdByGroupRoleList(String businessRoleName);

    boolean isOrderAcceptionAllowedForUser(Integer userId);

    UserRoleSettings retrieveSettingsForRole(Integer roleId);

    List<UserRoleSettings> retrieveSettingsForAllRoles();

    void updateSettingsForRole(UserRoleSettings settings);

    List<UserRole> getRolesAvailableForChangeByAdmin();


  List<UserRole> getRolesConsideredForPriceRangeComputation();

    List<UserRole> getRolesUsingRealMoney();
}

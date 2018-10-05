package com.exrates.inout.service.impl;

import com.exrates.inout.dao.UserRoleDao;
import com.exrates.inout.domain.enums.BusinessUserRoleEnum;
import com.exrates.inout.domain.enums.GroupUserRoleEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.main.UserRoleSettings;
import com.exrates.inout.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserRoleServiceImpl implements UserRoleService {
  @Autowired
  UserRoleDao userRoleDao;


  @Override
  @Transactional(readOnly = true)
  public List<UserRole> getRealUserRoleByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum) {
    return userRoleDao.findRealUserRoleIdByBusinessRoleList(businessUserRoleEnum.name());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getRealUserRoleIdByBusinessRoleList(BusinessUserRoleEnum businessUserRoleEnum) {
    return getRealUserRoleByBusinessRoleList(businessUserRoleEnum).stream()
        .map(e -> e.getRole())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public String[] getRealUserRoleNameByBusinessRoleArray(BusinessUserRoleEnum businessUserRoleEnum) {
    return getRealUserRoleByBusinessRoleList(businessUserRoleEnum).stream()
        .toArray(size->new String[size]);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getRealUserRoleIdByBusinessRoleList(String businessUserRoleName) {
    if ("ALL".equals(businessUserRoleName)) {
      return Collections.EMPTY_LIST;
    } else {
      BusinessUserRoleEnum businessUserRoleEnum = BusinessUserRoleEnum.convert(businessUserRoleName);
      return getRealUserRoleIdByBusinessRoleList(businessUserRoleEnum);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserRole> getRealUserRoleByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum) {
    return userRoleDao.findRealUserRoleIdByGroupRoleList(groupUserRoleEnum.name());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getRealUserRoleIdByGroupRoleList(GroupUserRoleEnum groupUserRoleEnum) {
    return getRealUserRoleByGroupRoleList(groupUserRoleEnum).stream()
        .map(e -> e.getRole())
        .collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getRealUserRoleIdByGroupRoleList(String groupUserRoleName) {
    GroupUserRoleEnum businessUserRoleEnum = GroupUserRoleEnum.convert(groupUserRoleName);
    return getRealUserRoleIdByGroupRoleList(businessUserRoleEnum);
  }


  @Override
  @Transactional(readOnly = true)
  public boolean isOrderAcceptionAllowedForUser(Integer userId) {
    return userRoleDao.isOrderAcceptionAllowedForUser(userId);
  }

  @Override
  @Transactional(readOnly = true)
  public UserRoleSettings retrieveSettingsForRole(Integer roleId) {
    return userRoleDao.retrieveSettingsForRole(roleId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserRole> getRolesAvailableForChangeByAdmin() {
    return userRoleDao.getRolesAvailableForChangeByAdmin();
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserRoleSettings> retrieveSettingsForAllRoles() {
    return userRoleDao.retrieveSettingsForAllRoles();
  }

  @Override
  @Transactional
  public void updateSettingsForRole(UserRoleSettings settings) {
    userRoleDao.updateSettingsForRole(settings);
  }

  @Override
  public List<UserRole> getRolesConsideredForPriceRangeComputation() {
    return userRoleDao.getRolesConsideredForPriceRangeComputation();
  }

  @Override
  public List<UserRole> getRolesUsingRealMoney() {
    return userRoleDao.getRolesUsingRealMoney();
  }



}

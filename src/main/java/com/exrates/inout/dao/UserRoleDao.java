package com.exrates.inout.dao;

import com.exrates.inout.domain.enums.UserRole;

import java.util.List;

public interface UserRoleDao {

    List<UserRole> findRealUserRoleIdByBusinessRoleList(String businessRoleName);

    List<UserRole> findRealUserRoleIdByGroupRoleList(String businessRoleName);
}

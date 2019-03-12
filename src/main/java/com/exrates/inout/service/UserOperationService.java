package com.exrates.inout.service;

import com.exrates.inout.domain.dto.UserOperationAuthorityOption;
import com.exrates.inout.domain.enums.UserOperationAuthority;

import java.util.List;
import java.util.Locale;

/**
 * @author Vlad Dziubak
 * Date: 01.08.2018
 */
public interface UserOperationService {

  boolean getStatusAuthorityForUserByOperation(int userId, UserOperationAuthority userOperationAuthority);

  List<UserOperationAuthorityOption> getUserOperationAuthorityOptions(Integer userId, Locale locale);

  void updateUserOperationAuthority(List<UserOperationAuthorityOption> options, Integer userId, String currentUserEmail);


}

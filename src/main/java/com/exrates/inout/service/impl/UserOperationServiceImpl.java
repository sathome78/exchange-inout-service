package com.exrates.inout.service.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.UserDao;
import com.exrates.inout.dao.UserOperationDao;
import com.exrates.inout.domain.dto.UserOperationAuthorityOption;
import com.exrates.inout.domain.enums.UserOperationAuthority;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.exceptions.ForbiddenOperationException;
import com.exrates.inout.service.UserOperationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//@Log4j2
@Service
public class UserOperationServiceImpl implements UserOperationService {

   private static final Logger log = LogManager.getLogger(UserOperationServiceImpl.class);


    @Autowired
    private UserOperationDao userOperationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean getStatusAuthorityForUserByOperation(int userId, UserOperationAuthority userOperationAuthority) {
        return userOperationDao.getStatusAuthorityForUserByOperation(userId, userOperationAuthority);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOperationAuthorityOption> getUserOperationAuthorityOptions(Integer userId, Locale locale) {
        return userOperationDao.getUserOperationAuthorityOption(userId).stream()
                .peek(option -> option.localize(messageSource, locale))
                .collect(Collectors.toList());
    }

    @Override
        public void updateUserOperationAuthority(List<UserOperationAuthorityOption> options, Integer userId, String currentUserEmail) {
        UserRole currentUserRole = userDao.getUserRoles(currentUserEmail);
        UserRole updatedUserRole = userDao.getUserRoleById(userId);
        if (currentUserRole != UserRole.ADMINISTRATOR && updatedUserRole == UserRole.ADMINISTRATOR) {
            throw new ForbiddenOperationException("Status modification not permitted");
        }
        userOperationDao.updateUserOperationAuthority(options, userId);
    }
}
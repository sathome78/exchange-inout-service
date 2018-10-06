package com.exrates.inout.service;

import com.exrates.inout.domain.dto.UpdateUserDto;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.TokenType;
import com.exrates.inout.domain.enums.UserCommentTopicEnum;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.main.TemporalToken;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.UserFile;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

public interface UserService {

    int getIdByEmail(String email);

    int getIdByNickname(String nickname);

    User findByEmail(String email);

    User findByNickname(String nickname);

    List<UserFile> findUserDoc(int userId);

    boolean isGlobal2FaActive();

    boolean create(User user, Locale locale, String source);

    boolean ifNicknameIsUnique(String nickname);

    boolean ifEmailIsUnique(String email);

    User getUserById(int id);

    boolean update(UpdateUserDto user, boolean resetPassword, Locale locale);

    boolean update(UpdateUserDto user, Locale locale);

    void sendEmailWithToken(User user, TokenType tokenType, String tokenLink, String emailSubject, String emailText, Locale locale);

    List<TemporalToken> getAllTokens();

    @Transactional(rollbackFor = Exception.class)
    void sendEmailWithToken(User user, TokenType tokenType, String tokenLink, String emailSubject, String emailText, Locale locale, String newPass, String... params);

    void sendUnfamiliarIpNotificationEmail(User user, String emailSubject, String emailText, Locale locale);

    boolean createTemporalToken(TemporalToken token);

    String getPreferedLang(int userId);

    String getPreferedLangByEmail(String email);

    Locale getUserLocaleForMobile(String email);

    boolean addUserComment(UserCommentTopicEnum topic, String newComment, String email, boolean sendMessage);

    UserRole getUserRoleFromSecurityContext();

    InvoiceOperationPermission getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(Integer userId, Integer currencyId, InvoiceOperationDirection invoiceOperationDirection);

    String getEmailById(Integer id);

    UserRole getUserRoleFromDB(Integer userId);

    UserRole getUserRoleFromDB(String email);

    @Transactional
    String updatePinForUserForEvent(String userEmail, NotificationMessageEventEnum event);

    boolean checkPin(String email, String pin, NotificationMessageEventEnum event);

    String getUserEmailFromSecurityContext();

    User getCommonReferralRoot();
}

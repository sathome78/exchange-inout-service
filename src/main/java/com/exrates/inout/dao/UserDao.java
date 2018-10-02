package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.UpdateUserDto;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.TokenType;
import com.exrates.inout.domain.enums.UserRole;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.main.Comment;
import com.exrates.inout.domain.main.TemporalToken;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.UserFile;

import java.nio.file.Path;
import java.util.List;

public interface UserDao {

    int getIdByNickname(String nickname);

    boolean create(User user);

    List<UserFile> findUserDoc(int userId);

    UserRole getUserRoleById(Integer id);

    User findByEmail(String email);

    boolean ifNicknameIsUnique(String nickname);

    boolean ifEmailIsUnique(String email);

    int getIdByEmail(String email);

    boolean update(UpdateUserDto user);

    User findByNickname(String nickname);

    User getUserById(int id);

    boolean createTemporalToken(TemporalToken token);

    TemporalToken verifyToken(String token);

    List<TemporalToken> getAllTokens();

    String getPreferredLang(int userId);

    String getPreferredLangByEmail(String email);

    boolean insertIp(String email, String ip);

    Long saveTemporaryPassword(Integer userId, String password, Integer tokenId);

    boolean addUserComment(Comment comment);

    InvoiceOperationPermission getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(Integer userId, Integer currencyId, InvoiceOperationDirection invoiceOperationDirection);

    String getEmailById(Integer id);

    String getPinByEmailAndEvent(String email, NotificationMessageEventEnum event);

    void updatePinByUserEmail(String userEmail, String pin, NotificationMessageEventEnum event);

}

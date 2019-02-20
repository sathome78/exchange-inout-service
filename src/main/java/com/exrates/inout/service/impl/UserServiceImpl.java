package com.exrates.inout.service.impl;


import com.exrates.inout.dao.UserDao;
import com.exrates.inout.domain.dto.UpdateUserDto;
import com.exrates.inout.domain.enums.*;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.service.NotificationService;
import com.exrates.inout.service.NotificationsSettingsService;
import com.exrates.inout.service.SendMailService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.token.TokenScheduler;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final SendMailService sendMailService;

    private final MessageSource messageSource;

    private final HttpServletRequest request;
    @Autowired
    private TokenScheduler tokenScheduler;

    private final NotificationsSettingsService settingsService;

    private boolean global2FaActive = false;

    @Autowired
    NotificationService notificationService;

    @Autowired
    public UserServiceImpl(UserDao userDao, SendMailService sendMailService, MessageSource messageSource, HttpServletRequest request, NotificationsSettingsService settingsService) {
        this.userDao = userDao;
        this.sendMailService = sendMailService;
        this.messageSource = messageSource;
        this.request = request;
        this.settingsService = settingsService;
    }

    @Override
    public boolean isGlobal2FaActive() {
        return global2FaActive;
    }

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Set<String> USER_ROLES = Stream.of(UserRole.values()).map(UserRole::name).collect(Collectors.toSet());
    private final UserRole ROLE_DEFAULT_COMMISSION = UserRole.USER;

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Transactional(rollbackFor = Exception.class)
    public boolean create(User user, Locale locale, String source) {
        Boolean flag = false;
        if (this.ifEmailIsUnique(user.getEmail())) {
            if (this.ifNicknameIsUnique(user.getNickname())) {
                if (userDao.create(user) && userDao.insertIp(user.getEmail(), user.getIp())) {
                    int user_id = this.getIdByEmail(user.getEmail());
                    user.setId(user_id);
                    if (source != null && !source.isEmpty()) {
                        String view = "view=" + source;
                        sendEmailWithToken(user, TokenType.REGISTRATION, "/registrationConfirm", "emailsubmitregister.subject", "emailsubmitregister.text", locale, null, view);
                    } else {
                        sendEmailWithToken(user, TokenType.REGISTRATION, "/registrationConfirm", "emailsubmitregister.subject", "emailsubmitregister.text", locale);
                    }
                    flag = true;
                }
            }
        }
        return flag;
    }

    public List<TemporalToken> getAllTokens() {
        return userDao.getAllTokens();
    }

    public int getIdByEmail(String email) {
        return userDao.getIdByEmail(email);
    }

    public int getIdByNickname(String nickname) {
        return userDao.getIdByNickname(nickname);
    }

    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User findByNickname(String nickname) {
        return userDao.findByNickname(nickname);
    }

    public boolean ifNicknameIsUnique(String nickname) {
        return userDao.ifNicknameIsUnique(nickname);
    }

    public boolean ifEmailIsUnique(String email) {
        return userDao.ifEmailIsUnique(email);
    }

    private String generateRegistrationToken() {
        return UUID.randomUUID().toString();
    }

    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(UpdateUserDto user, boolean resetPassword, Locale locale) {
        boolean changePassword = user.getPassword() != null && !user.getPassword().isEmpty();
        boolean changeFinPassword = user.getFinpassword() != null && !user.getFinpassword().isEmpty();

        if (userDao.update(user)) {
            User u = new User();
            u.setId(user.getId());
            u.setEmail(user.getEmail());
            if (changePassword) {
                sendUnfamiliarIpNotificationEmail(u, "admin.changePasswordTitle", "user.settings.changePassword.successful", locale);
            } else if (changeFinPassword) {
                sendEmailWithToken(u, TokenType.CHANGE_FIN_PASSWORD, "/changeFinPasswordConfirm", "emailsubmitChangeFinPassword.subject", "emailsubmitChangeFinPassword.text", locale);
            } else if (resetPassword) {
                sendEmailWithToken(u, TokenType.CHANGE_PASSWORD, "/resetPasswordConfirm", "emailsubmitResetPassword.subject", "emailsubmitResetPassword.text", locale);
            }
        }
        return true;
    }

    public boolean update(UpdateUserDto user, Locale locale) {
        return update(user, false, locale);
    }

    public void sendEmailWithToken(User user, TokenType tokenType, String tokenLink, String emailSubject, String emailText, Locale locale) {
        sendEmailWithToken(user, tokenType, tokenLink, emailSubject, emailText, locale, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void sendEmailWithToken(User user, TokenType tokenType, String tokenLink, String emailSubject, String emailText, Locale locale, String tempPass, String... params) {
        TemporalToken token = new TemporalToken();
        token.setUserId(user.getId());
        token.setValue(generateRegistrationToken());
        token.setTokenType(tokenType);
        token.setCheckIp(user.getIp());
        token.setAlreadyUsed(false);

        createTemporalToken(token);
        String tempPassId = "";
        if (tempPass != null) {
            tempPassId = "&tempId=" + userDao.saveTemporaryPassword(user.getId(), tempPass, userDao.verifyToken(token.getValue()).getId());
        }

        Email email = new Email();
        StringBuilder confirmationUrl = new StringBuilder(tokenLink + "?token=" + token.getValue() + tempPassId);
        if (tokenLink.equals("/resetPasswordConfirm")) {
            confirmationUrl.append("&email=").append(user.getEmail());
        }
        String rootUrl = "";
        if (!confirmationUrl.toString().contains("//")) {
            rootUrl = request.getScheme() + "://" + request.getServerName() +
                    ":" + request.getServerPort();
        }
        if (params != null) {
            for (String patram : params) {
                confirmationUrl.append("&").append(patram);
            }
        }
        email.setMessage(
                messageSource.getMessage(emailText, null, locale) +
                        " <a href='" +
                        rootUrl +
                        confirmationUrl.toString() +
                        "'>" + messageSource.getMessage("admin.ref", null, locale) + "</a>"
        );
        email.setSubject(messageSource.getMessage(emailSubject, null, locale));

        email.setTo(user.getEmail());
        if (tokenType.equals(TokenType.REGISTRATION)
                || tokenType.equals(TokenType.CHANGE_PASSWORD)
                || tokenType.equals(TokenType.CHANGE_FIN_PASSWORD)) {
            sendMailService.sendMailMandrill(email);
        } else {
            sendMailService.sendMail(email);
        }
    }

    public void sendUnfamiliarIpNotificationEmail(User user, String emailSubject, String emailText, Locale locale) {
        Email email = new Email();
        email.setTo(user.getEmail());
        email.setMessage(messageSource.getMessage(emailText, new Object[]{user.getIp()}, locale));
        email.setSubject(messageSource.getMessage(emailSubject, null, locale));
        sendMailService.sendInfoMail(email);
    }

    public boolean createTemporalToken(TemporalToken token) {
        log.info("Token is " + token);
        boolean result = userDao.createTemporalToken(token);
        if (result) {
            log.info("Token succesfully saved");
            tokenScheduler.initTrigers();
        }
        return result;
    }

    public String getPreferedLang(int userId) {
        return userDao.getPreferredLang(userId);
    }

    public String getPreferedLangByEmail(String email) {
        return userDao.getPreferredLangByEmail(email);
    }

    @PostConstruct
    private void initTokenTriggers() {
        tokenScheduler.initTrigers();
    }

    public Locale getUserLocaleForMobile(String email) {
        String lang = getPreferedLangByEmail(email);
        //adaptation for locales available in mobile app
        if (!("ru".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang))) {
            lang = "en";
        }

        return new Locale(lang);
    }

    public boolean addUserComment(UserCommentTopicEnum topic, String newComment, String email, boolean sendMessage) {

        User user = findByEmail(email);
        User creator;
        Comment comment = new Comment();
        comment.setMessageSent(sendMessage);
        comment.setUser(user);
        comment.setComment(newComment);
        comment.setUserCommentTopic(topic);
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            creator = findByEmail(auth.getName());
            comment.setCreator(creator);
        } catch (Exception e) {
            LOGGER.error(e);
        }
        boolean success = userDao.addUserComment(comment);

        if (comment.isMessageSent()) {
            notificationService.notifyUser(user.getId(), NotificationEvent.ADMIN, "admin.subjectCommentTitle",
                    "admin.subjectCommentMessage", new Object[]{": " + newComment});
        }

        return success;
    }

    public UserRole getUserRoleFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String grantedAuthority = authentication.getAuthorities().
                stream().map(GrantedAuthority::getAuthority)
                .filter(USER_ROLES::contains)
                .findFirst().orElse(ROLE_DEFAULT_COMMISSION.name());
        LOGGER.debug("Granted authority: " + grantedAuthority);
        return UserRole.valueOf(grantedAuthority);
    }

    @Transactional(readOnly = true)
    public InvoiceOperationPermission getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(
            Integer userId,
            Integer currencyId,
            InvoiceOperationDirection invoiceOperationDirection) {
        return userDao.getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(userId, currencyId, invoiceOperationDirection);
    }

    @Transactional(readOnly = true)
    public String getEmailById(Integer id) {
        return userDao.getEmailById(id);
    }

    @Transactional
    public UserRole getUserRoleFromDB(Integer userId) {
        return userDao.getUserRoleById(userId);
    }

    @Transactional
    public String updatePinForUserForEvent(String userEmail, NotificationMessageEventEnum event) {
        String pin = String.valueOf(10000000 + new Random().nextInt(90000000));
        userDao.updatePinByUserEmail(userEmail, passwordEncoder.encode(pin), event);
        return pin;
    }

    public boolean checkPin(String email, String pin, NotificationMessageEventEnum event) {
        int userId = getIdByEmail(email);
        NotificationsUserSetting setting = settingsService.getByUserAndEvent(userId, event);
        if ((setting == null || setting.getNotificatorId() == null) && !event.isCanBeDisabled()) {
            setting = NotificationsUserSetting.builder()
                    .notificatorId(NotificationTypeEnum.EMAIL.getCode())
                    .userId(userId)
                    .notificationMessageEventEnum(event)
                    .build();
        }
        return passwordEncoder.matches(pin, getPinForEvent(email, event));
    }

    @Override
    public User getCommonReferralRoot() {
        try {
            return userDao.getCommonReferralRoot();
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void blockUserByRequest(Integer userId) {
        throw new NotImplementedException();//TODO ask Maks
    }


    private String getPinForEvent(String email, NotificationMessageEventEnum event) {
        return userDao.getPinByEmailAndEvent(email, event);
    }
}
package com.exrates.inout.service.impl;

import com.exrates.inout.dao.NotificationDao;
import com.exrates.inout.domain.dto.NotificationDto;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.service.NotificationService;
import com.exrates.inout.service.SendMailService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.util.Cache;
import com.exrates.inout.util.CacheData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {


    @Autowired
    private NotificationDao notificationDao;

    @Autowired
    private UserService userService;

    @Autowired
    private SendMailService sendMailService;

    @Autowired
    private MessageSource messageSource;

    // TODO manage notifications in admin page


    /*private long createNotification(Integer userId, String title, String message, NotificationEvent cause) {
        Notification notification = new Notification();
        notification.setReceiverUserId(userId);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setCause(cause);
        return notificationDao.createNotification(notification);
    }*/

    @Override
    public long createLocalizedNotification(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                                            Object[] messageArgs) {
        Locale locale = new Locale(userService.getPreferedLang(userId));
        return 0L /*createNotification(userId, messageSource.getMessage(titleCode, null, locale),
                messageSource.getMessage(messageCode, normalizeArgs(messageArgs), locale), cause)*/;

    }

    @Override
    public long createLocalizedNotification(String userEmail, NotificationEvent cause, String titleCode, String messageCode,
                                            Object[] messageArgs) {
        Integer userId = userService.getIdByEmail(userEmail);
        Locale locale = new Locale(userService.getPreferedLang(userId));
        return 0L /*createNotification(userId, messageSource.getMessage(titleCode, null, locale),
                messageSource.getMessage(messageCode, normalizeArgs(messageArgs), locale), cause)*/;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs) {
        notifyUser(userService.getIdByEmail(email), cause, titleCode, messageCode, normalizeArgs(messageArgs));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(String email, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs, Locale locale) {
        notifyUser(userService.getIdByEmail(email), cause, titleCode, messageCode, normalizeArgs(messageArgs), locale);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs) {
        String lang = userService.getPreferedLang(userId);
        Locale locale = new Locale(StringUtils.isEmpty(lang) ? "EN" : lang);
        notifyUser(userId, cause, titleCode, messageCode, normalizeArgs(messageArgs), locale);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleCode, String messageCode,
                           Object[] messageArgs, Locale locale) {
        String titleMessage = messageSource.getMessage(titleCode, null, locale);
        String message = messageSource.getMessage(messageCode, normalizeArgs(messageArgs), locale);
        notifyUser(userId, cause, titleMessage, message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyUser(Integer userId, NotificationEvent cause, String titleMessage, String message) {
        User user = userService.getUserById(userId);
        /*NotificationOption option = notificationDao.findUserOptionForEvent(userId, cause);*/
      /*if (option.isSendNotification()) {
        createNotification(
            userId,
            titleMessage,
            message,
            cause);
      }*/
        /*Always on email notifications*/
        if (true/*option.isSendEmail()*/) {
            Email email = new Email();
            email.setSubject(titleMessage);
            email.setMessage(message);
            email.setTo(user.getEmail());
            sendMailService.sendInfoMail(email);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<Notification> findAllByUser(String email) {
        return notificationDao.findAllByUser(userService.getIdByEmail(email));
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> findByUser(String email, CacheData cacheData, Integer offset, Integer limit) {
        List<NotificationDto> result = notificationDao.findByUser(userService.getIdByEmail(email), offset, limit);
        if (Cache.checkCache(cacheData, result)) {
            result = new ArrayList<NotificationDto>() {{
                add(new NotificationDto(false));
            }};
        }
        return result;
    }

    @Override
    public boolean setRead(Long notificationId) {
        return notificationDao.setRead(notificationId);
    }

    @Override
    public boolean remove(Long notificationId) {
        return notificationDao.remove(notificationId);
    }

    @Override
    public int setReadAllByUser(String email) {
        return notificationDao.setReadAllByUser(userService.getIdByEmail(email));
    }

    @Override
    public int removeAllByUser(String email) {
        return notificationDao.removeAllByUser(userService.getIdByEmail(email));

    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationOption> getNotificationOptionsByUser(Integer userId) {
        return notificationDao.getNotificationOptionsByUser(userId);
    }

    @Override
    public void updateUserNotifications(List<NotificationOption> options) {
        notificationDao.updateNotificationOptions(options);
    }

    private String[] normalizeArgs(Object... args) {
        return Arrays.toString(args).replaceAll("[\\[\\]]", "").split("\\s*,\\s*");
    }

    @Override
    public void updateNotificationOptionsForUser(int userId, List<NotificationOption> options) {
        throw new UnsupportedOperationException();
    }
}
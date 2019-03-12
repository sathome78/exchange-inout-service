//package com.exrates.inout.service.impl;
//
//import com.exrates.inout.domain.dto.NotificationResultDto;
//import com.exrates.inout.domain.dto.PinAttempsDto;
//import com.exrates.inout.domain.dto.PinDto;
//import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
//import com.exrates.inout.domain.main.NotificationsUserSetting;
//import com.exrates.inout.domain.main.User;
//import com.exrates.inout.exceptions.PinCodeCheckNeedException;
//import com.exrates.inout.filter.CapchaAuthorizationFilter;
//import com.exrates.inout.service.NotificationMessageService;
//import com.exrates.inout.service.NotificationsSettingsService;
//import com.exrates.inout.service.SecureService;
//import com.exrates.inout.service.UserService;
//import com.google.common.collect.ObjectArrays;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.security.core.Authentication;
//import org.springframework.stereotype.Service;
//import org.springframework.web.servlet.LocaleResolver;
//import sun.reflect.generics.reflectiveObjects.NotImplementedException;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.Locale;
//
//
//@Log4j2
//@Service("secureServiceImpl")
//public class SecureServiceImpl implements SecureService {
//    private @Value("${session.checkPinParam}") String checkPinParam;
//    private @Value("${session.authenticationParamName}") String authenticationParamName;
//    private @Value("${session.passwordParam}") String passwordParam;
//
//    @Autowired
//    private NotificationMessageService notificationService;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private LocaleResolver localeResolver;
//    @Autowired
//    private MessageSource messageSource;
//    @Autowired
//    private NotificationsSettingsService settingsService;
//
//
//    @Override
//    public void checkLoginAuth(HttpServletRequest request, Authentication authentication,
//                               CapchaAuthorizationFilter filter) {
//        request.getSession().setAttribute("2fa_".concat(NotificationMessageEventEnum.LOGIN.name()), new PinAttempsDto());
//        PinDto result = reSendLoginMessage(request, authentication.getName(), false);
//        if (result != null) {
//            request.getSession().setAttribute(checkPinParam, "");
//            request.getSession().setAttribute(authenticationParamName, authentication);
//            request.getSession().setAttribute(passwordParam, request.getParameter(filter.getPasswordParameter()));
//            authentication.setAuthenticated(false);
//            throw new PinCodeCheckNeedException(result.getMessage());
//        }
//    }
//
//    @Override
//    public PinDto reSendLoginMessage(HttpServletRequest request, String userEmail, boolean forceSend) {
//        throw new NotImplementedException();
//    }
//
//
//    /*Method used For withdraw or transfer*/
//    @Override
//    public void checkEventAdditionalPin(HttpServletRequest request, String email,
//                                        NotificationMessageEventEnum event, String amountCurrency) {
//        request.getSession().setAttribute("2fa_".concat(event.name()), new PinAttempsDto());
//        PinDto result = resendEventPin(request, email, event, amountCurrency);
//        if (result != null) {
//            throw new PinCodeCheckNeedException(result.getMessage());
//        }
//    }
//
//    @Override
//    public PinDto resendEventPin(HttpServletRequest request, String email, NotificationMessageEventEnum event, String amountCurrency) {
//        throw new NotImplementedException();
//    }
//
//    private String sendPinMessage(String email, NotificationsUserSetting setting, HttpServletRequest request, String[] args) {
//        Locale locale = localeResolver.resolveLocale(request);
//        String subject = messageSource.getMessage(setting.getNotificationMessageEventEnum().getSbjCode(), null, locale);
//        String[] pin = new String[]{userService.updatePinForUserForEvent(email, setting.getNotificationMessageEventEnum())};
//        String messageText = messageSource.getMessage(setting.getNotificationMessageEventEnum().getMessageCode(),
//                ObjectArrays.concat(pin, args, String.class), locale);
//        NotificationResultDto notificationResultDto = notificationService.notifyUser(email, messageText, subject, setting);
//        request.getSession().setAttribute("2fa_message".concat(setting.getNotificationMessageEventEnum().name()), notificationResultDto);
//        return messageSource.getMessage(notificationResultDto.getMessageSource(), notificationResultDto.getArguments(), locale);
//    }
//
//    private NotificationsUserSetting getWithdrawSettings(User user) {
//        return  NotificationsUserSetting
//                .builder()
//                .notificationMessageEventEnum(NotificationMessageEventEnum.WITHDRAW)
//                .notificatorId(NotificationMessageEventEnum.WITHDRAW.getCode())
//                .userId(user.getId())
//                .build();
//    }
//
//    private NotificationsUserSetting getLoginSettings(User user) {
//        return  NotificationsUserSetting
//                .builder()
//                .notificationMessageEventEnum(NotificationMessageEventEnum.LOGIN)
//                .notificatorId(NotificationMessageEventEnum.LOGIN.getCode())
//                .userId(user.getId())
//                .build();
//    }
//}

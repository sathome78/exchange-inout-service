package com.exrates.inout.service;

import com.exrates.inout.domain.dto.PinDto;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.filter.CapchaAuthorizationFilter;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface SecureService {

    void checkLoginAuth(HttpServletRequest request, Authentication authentication,
                        CapchaAuthorizationFilter filter);

    PinDto reSendLoginMessage(HttpServletRequest request, String userEmail, boolean forceSend);

    void checkEventAdditionalPin(HttpServletRequest request, String email, NotificationMessageEventEnum event, String amountCurrency);

    PinDto resendEventPin(HttpServletRequest request, String email, NotificationMessageEventEnum event, String amountCurrency);
}

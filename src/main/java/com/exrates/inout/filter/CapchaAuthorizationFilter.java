package com.exrates.inout.filter;

import com.exrates.inout.domain.dto.PinDto;
import com.exrates.inout.domain.enums.IpTypesOfChecking;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.UserStatus;
import com.exrates.inout.exceptions.BannedIpException;
import com.exrates.inout.exceptions.IncorrectPinException;
import com.exrates.inout.exceptions.NotVerifiedCaptchaError;
import com.exrates.inout.exceptions.UnconfirmedUserException;
import com.exrates.inout.service.IpBlockingService;
import com.exrates.inout.service.SecureService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.util.GeetestLib;
import com.exrates.inout.util.IpUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Log4j2
public class CapchaAuthorizationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;
    @Autowired
    private UserService userService;
    @Autowired
    private SecureService secureServiceImpl;

    @Autowired
    private IpBlockingService ipBlockingService;

    @Autowired
    private GeetestLib geetest;
    @Value("${session.checkPinParam}")
    private String checkPinParam;
    @Value("${session.pinParam}")
    private String pinParam;
    @Value("${session.passwordParam}")
    private String passwordParam;
    private String authenticationParamName = "authentication";


    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        HttpSession session = request.getSession(false);
        /*----------------------------*/
        String ipAddress = IpUtils.getClientIpAddress(request);
        try {
            ipBlockingService.checkIp(ipAddress, IpTypesOfChecking.LOGIN);
        } catch (BannedIpException e) {
            long banDuration = e.getBanDurationSeconds();
            String durationMessage;
            if (banDuration < 60 * 60) {
                String durationMinutes = String.valueOf(banDuration / 60);
                durationMessage = messageSource.getMessage("ip.ban.time.minutes", new Object[]{durationMinutes}, localeResolver.resolveLocale(request));
            } else {
                String durationHours = String.valueOf(banDuration / (60 * 60));
                durationMessage = messageSource.getMessage("ip.ban.time.hours", new Object[]{durationHours},
                        localeResolver.resolveLocale(request));
            }

            // rethrow to add localization message
            throw new BannedIpException(messageSource.getMessage("ip.ban.message", new Object[]{durationMessage},
                    localeResolver.resolveLocale(request)), banDuration);
        }

        if (session.getAttribute(checkPinParam) != null && request.getParameter(pinParam) != null
                && request.getParameter(super.getUsernameParameter()) == null
                && request.getParameter(super.getPasswordParameter()) == null
                && session.getAttribute(authenticationParamName) != null) {
            Authentication authentication = (Authentication) session.getAttribute(authenticationParamName);
            User principal = (User) authentication.getPrincipal();
            if (!userService.checkPin(principal.getUsername(), request.getParameter(pinParam), NotificationMessageEventEnum.LOGIN)) {
                PinDto res = secureServiceImpl.reSendLoginMessage(request, authentication.getName(), true);
                throw new IncorrectPinException(res);
            }
            return attemptAuthentication(principal.getUsername(),
                    String.valueOf(session.getAttribute(passwordParam)), request, response);
        } else {
//            String captchaType = request.getParameter("captchaType");
//            if (captchaType == null) {
//                throw new NotVerifiedCaptchaError( messageSource.getMessage("register.capchaincorrect", null, localeResolver.resolveLocale(request)));
//            }
//            switch (captchaType) {
//                case "BOTDETECT": {
//                    String captchaId = request.getParameter("captchaId");
//                    Captcha captcha = Captcha.load(request, captchaId);
//                    String captchaCode = request.getParameter("captchaCode");
//                    if (!captcha.validate(captchaCode)) {
//                        String correctCapchaRequired = messageSource.getMessage("register.capchaincorrect", null, localeResolver.resolveLocale(request));
//                        throw new NotVerifiedCaptchaError(correctCapchaRequired);
//                    }
//                    break;
//                }
//                case "RECAPTCHA": {
//                    String recapchaResponse = request.getParameter("g-recaptcha-response");
//                    if ((recapchaResponse != null) && !verifyReCaptchaSec.verify(recapchaResponse)) {
//                        String correctCapchaRequired = messageSource.getMessage("register.capchaincorrect", null, localeResolver.resolveLocale(request));
//                        throw new NotVerifiedCaptchaError(correctCapchaRequired);
//                    }
//                    break;
//                }
//            }

            String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
            String validate = request.getParameter(GeetestLib.fn_geetest_validate);
            String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

            int gt_server_status_code = (Integer) request.getSession().getAttribute(geetest.gtServerStatusSessionKey);
            String userid = (String) request.getSession().getAttribute("userid");

            HashMap<String, String> param = new HashMap<>();
            param.put("user_id", userid);

            int gtResult = 0;
            String correctCapchaRequired = messageSource.getMessage("register.capchaincorrect", null, localeResolver.resolveLocale(request));

            if (gt_server_status_code == 1) {
                gtResult = geetest.enhencedValidateRequest(challenge, validate, seccode, param);
                logger.error(gtResult);
            } else {
                logger.error("failback:use your own server captcha validate");
                gtResult = geetest.failbackValidateRequest(challenge, validate, seccode);
                logger.error(gtResult);
                throw new NotVerifiedCaptchaError(correctCapchaRequired);
            }

            if (gtResult != 1) {
                logger.error(gtResult);
                throw new NotVerifiedCaptchaError(correctCapchaRequired);
            }
        }
        if (userService.findByEmail(request.getParameter("username")).getStatus() == UserStatus.REGISTERED) {
            throw new UnconfirmedUserException(userService.findByEmail(request.getParameter("username")).getEmail());
        }
        /*---------------*/
        Authentication authentication = super.attemptAuthentication(request, response);
        /*-------------------*/
        secureServiceImpl.checkLoginAuth(request, authentication, this);
        /* old impl
        User principal = (User) authentication.getPrincipal();
        if (userService.isGlobal2FaActive() || userService.getUse2Fa(principal.getUsername())) {
            userService.createSendAndSaveNewPinForUser(principal.getUsername(), request);
            request.getSession().setAttribute(checkPinParam, "");
            request.getSession().setAttribute(authenticationParamName, authentication);
            request.getSession().setAttribute(passwordParam, request.getParameter(super.getPasswordParameter()));
            authentication.setAuthenticated(false);
            throw new PinCodeCheckNeedException("");
        }*/
        return authentication;
    }


    private Authentication attemptAuthentication(String username, String password, HttpServletRequest request,
                                                 HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        username = username.trim();
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
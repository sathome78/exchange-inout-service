package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawRequestInfoDto;
import com.exrates.inout.domain.dto.WithdrawRequestParamsDto;
import com.exrates.inout.domain.main.ClientBank;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.exceptions.entity.ErrorInfo;
import com.exrates.inout.service.InputOutputService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.WithdrawService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.exrates.inout.util.RequestUtils.extractUserRole;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class WithdrawRequestController {

    private static final Logger log = LogManager.getLogger("withdraw");

    private final MessageSource messageSource;
    private final WithdrawService withdrawService;
    private final UserService userService;
    private final MerchantService merchantService;
    private final InputOutputService inputOutputService;
    private final LocaleResolver localeResolver;

    private final static String withdrawRequestSessionAttr = "withdrawRequestCreateDto";

    @Autowired
    public WithdrawRequestController(MessageSource messageSource, WithdrawService withdrawService, UserService userService, MerchantService merchantService, InputOutputService inputOutputService, LocaleResolver localeResolver) {
        this.messageSource = messageSource;
        this.withdrawService = withdrawService;
        this.userService = userService;
        this.merchantService = merchantService;
        this.inputOutputService = inputOutputService;
        this.localeResolver = localeResolver;
    }

    @RequestMapping(value = "/withdraw/request/create", method = POST)
    @ResponseBody
    public Map<String, String> createWithdrawalRequest(
            @RequestBody WithdrawRequestParamsDto requestParamsDto,
            Principal principal, HttpServletRequest request,
            Locale locale) throws Exception {
        WithdrawRequestCreateDto withdrawRequestCreateDto = withdrawService.prepareWithdrawRequest(requestParamsDto, userService.getIdByEmail(principal.getName()), locale, extractUserRole(request));
        return withdrawService.createWithdrawalRequest(withdrawRequestCreateDto, locale);
    }

    private String getAmountWithCurrency(WithdrawRequestCreateDto dto) {
        return String.join("", dto.getAmount().stripTrailingZeros().toPlainString(), " ", dto.getCurrencyName());
    }
//
//    @RequestMapping(value = "/withdraw/request/pin", method = POST)
//    @ResponseBody
//    public Map<String, String> withdrawRequestCheckPin(
//            @RequestParam String pin, Locale locale, HttpServletRequest request, Principal principal) {
//        log.debug("withdraw pin {}", pin);
//        Object object = request.getSession().getAttribute(withdrawRequestSessionAttr);
//        Preconditions.checkNotNull(object);
//        Preconditions.checkArgument(pin.length() > 2 && pin.length() < 15);
//        if (userService.checkPin(principal.getName(), pin, NotificationMessageEventEnum.WITHDRAW)) {
//            request.getSession().removeAttribute(withdrawRequestSessionAttr);
//            return withdrawService.createWithdrawalRequest((WithdrawRequestCreateDto) object, locale);
//        } else {
//            PinDto res = secureServiceImpl.resendEventPin(request, principal.getName(),
//                    NotificationMessageEventEnum.WITHDRAW, getAmountWithCurrency((WithdrawRequestCreateDto) object));
//            throw new IncorrectPinException(res);
//        }
//    }

    @RequestMapping(value = "/withdraw/request/revoke", method = POST)
    @ResponseBody
    public void revokeWithdrawRequest(@RequestParam Integer id) {
        withdrawService.revokeWithdrawalRequest(id);
    }

    @RequestMapping(value = "/withdraw/banks", method = GET)
    @ResponseBody
    public List<ClientBank> getBankListForCurrency(
            @RequestParam Integer currencyId) {
        return withdrawService.findClientBanksForCurrency(currencyId);
    }

    @RequestMapping(value = "/withdraw/commission", method = GET)
    @ResponseBody
    public Map<String, String> getCommissions(
            HttpServletRequest request,
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("currency") Integer currencyId, @RequestParam("merchant") Integer merchantId,
            @RequestParam(value = "memo", required = false) String memo, Principal principal, Locale locale) throws CheckDestinationTagException {
        Integer userId = userService.getIdByEmail(principal.getName());
        if (!StringUtils.isEmpty(memo)) {
            merchantService.checkDestinationTag(merchantId, memo);
        }
        return withdrawService.correctAmountAndCalculateCommissionPreliminarily(userId, amount, currencyId, merchantId, locale, memo, extractUserRole(request));
    }

    @RequestMapping(value = "/withdraw/info", method = GET)
    @ResponseBody
    public WithdrawRequestInfoDto getWithdrawRequestInfo(@RequestParam Integer requestId, HttpServletRequest request) {
        /*Preconditions.checkArgument(principal.getName().equalsIgnoreCase(infoDto.getUserEmail()));*/
        return withdrawService.getWithdrawalInfo(requestId, localeResolver.resolveLocale(request));
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/take", method = POST)
    @ResponseBody
    public void takeToWork(@RequestParam Integer id, Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        withdrawService.takeInWorkWithdrawalRequest(id, requesterAdminId);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/return", method = POST)
    @ResponseBody
    public void returnFromWork(@RequestParam Integer id, Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        withdrawService.returnFromWorkWithdrawalRequest(id, requesterAdminId);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/decline", method = POST)
    @ResponseBody
    public void decline(
            @RequestParam Integer id,
            @RequestParam String comment,
            Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        withdrawService.declineWithdrawalRequest(id, requesterAdminId, comment);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/confirm", method = POST)
    @ResponseBody
    public void confirm(
            @RequestParam Integer id,
            Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        withdrawService.confirmWithdrawalRequest(id, requesterAdminId);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/post", method = POST)
    @ResponseBody
    public void postHolded(
            @RequestParam Integer id,
            @RequestParam String txHash,
            Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        withdrawService.postWithdrawalRequest(id, requesterAdminId, txHash);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/statistic", method = POST)
    @ResponseBody
    public List<Integer> statistic(
            @RequestParam String startDate,
            @RequestParam String endDate) {

        return withdrawService.getWithdrawalStatistic(startDate, endDate);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseBody
    public ErrorInfo NotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({
            InvoiceActionIsProhibitedForCurrencyPermissionOperationException.class,
            InvoiceActionIsProhibitedForNotHolderException.class
    })
    @ResponseBody
    public ErrorInfo ForbiddenExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler({
            RequestLimitExceededException.class
    })
    @ResponseBody
    public ErrorInfo RequestLimitExceededExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({
            CheckDestinationTagException.class
    })
    @ResponseBody
    public ErrorInfo CheckDestinationTagExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(),
                exception, messageSource.getMessage(exception.getMessage(),
                new String[]{((CheckDestinationTagException) exception).getFieldName()}, localeResolver.resolveLocale(req)));
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({
            NotEnoughUserWalletMoneyException.class
    })
    @ResponseBody
    public ErrorInfo NotEnoughUserWalletMoneyExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception, messageSource
                .getMessage("merchants.notEnoughWalletMoney", null, localeResolver.resolveLocale(req)));
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({AbsentFinPasswordException.class, NotConfirmedFinPasswordException.class})
    @ResponseBody
    public ErrorInfo finPassExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ErrorInfo(req.getRequestURL(), exception, messageSource.getMessage(((MerchantException) (exception)).getReason(), null, localeResolver.resolveLocale(req)));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo OtherErrorsHandler(HttpServletRequest req, Exception exception) {
        if (exception instanceof MerchantException) {
            return new ErrorInfo(req.getRequestURL(), exception,
                    messageSource.getMessage(((MerchantException) (exception)).getReason(), null, localeResolver.resolveLocale(req)));
        }
        log.error(ExceptionUtils.getStackTrace(exception));
        return new ErrorInfo(req.getRequestURL(), exception);
    }

}

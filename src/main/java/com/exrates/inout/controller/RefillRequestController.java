package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.main.InvoiceBank;
import com.exrates.inout.domain.other.PaginationWrapper;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.exceptions.entity.ErrorInfo;
import com.exrates.inout.service.InputOutputService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.UserOperationService;
import com.exrates.inout.service.UserService;
import org.apache.commons.lang.exception.ExceptionUtils;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class RefillRequestController {

    private static final Logger log = LogManager.getLogger("refill");

    private final MessageSource messageSource;

    private final RefillService refillService;

    private final UserService userService;

    private final InputOutputService inputOutputService;

    private final LocaleResolver localeResolver;

    private final UserOperationService userOperationService;

    @Autowired
    public RefillRequestController(MessageSource messageSource, RefillService refillService, UserService userService, InputOutputService inputOutputService, LocaleResolver localeResolver, UserOperationService userOperationService) {
        this.messageSource = messageSource;
        this.refillService = refillService;
        this.userService = userService;
        this.inputOutputService = inputOutputService;
        this.localeResolver = localeResolver;
        this.userOperationService = userOperationService;
    }

    @RequestMapping(value = "/refill/request/create", method = POST)
    @ResponseBody
    public Map<String, Object> createRefillRequest(
            @RequestBody RefillRequestParamsDto requestParamsDto,
            Principal principal,
            Locale locale, HttpServletRequest servletRequest) {
        try {
            return refillService.prepareRefillRequestCreateDto(requestParamsDto, principal.getName(), locale);
        } catch (ForceGenerationAddressException e) {
            return new HashMap<String, Object>() {{
                put("address", e.getAddress());
                put("message", e.getMessage());
                put("qr", e.getAddress());
            }};
        }
    }


    @RequestMapping(value = "/refill/request/revoke", method = POST)
    @ResponseBody
    public void revokeWithdrawRequest(@RequestParam Integer id) {
        refillService.revokeRefillRequest(id);
    }

    @GetMapping(value = "/refill/request/info")
    public RefillRequestFlatDto getInfoRefill(@RequestParam Integer id) {
        return refillService.getFlatById(id);
    }


    @RequestMapping(value = "/refill/request/confirm", method = POST)
    @ResponseBody
    public void confirmWithdrawRequest(InvoiceConfirmData invoiceConfirmData, Locale locale) {
        refillService.confirmRefillRequest(invoiceConfirmData, locale);
    }

    @GetMapping(value = "/refill/banks")
    public List<InvoiceBank> getBankListForCurrency(@RequestParam Integer currencyId) {
        List<InvoiceBank> banks = refillService.findBanksForCurrency(currencyId);
        banks.forEach(bank -> {
            if (bank.getBankDetails() != null) {
                bank.setBankDetails(bank.getBankDetails().replaceAll("\n", "<br/>"));
            }
        });
        return banks;
    }

    @GetMapping(value = "/refill/commission")
    public Map<String, String> getCommissions(@RequestParam("amount") BigDecimal amount,
            @RequestParam("currency") Integer currencyId,
            @RequestParam("merchant") Integer merchantId,
            Principal principal, Locale locale) {
        Integer userId = userService.getIdByEmail(principal.getName());
        return refillService.correctAmountAndCalculateCommission(userId, amount, currencyId, merchantId, locale);
    }

    @GetMapping(value = "/refill/unconfirmed")
    public PaginationWrapper<List<MyInputOutputHistoryDto>> findMyUnconfirmedRefillRequests(@RequestParam("currency") String currencyName,
                                                                                            @RequestParam("limit") Integer limit,
                                                                                            @RequestParam("offset") Integer offset,
                                                                                            Principal principal, Locale locale) {
        return inputOutputService.findUnconfirmedInvoices(principal.getName(), currencyName, limit, offset, locale);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/refill/take", method = POST)
    @ResponseBody
    public void takeToWork(
            @RequestParam Integer id,
            Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        refillService.takeInWorkRefillRequest(id, requesterAdminId);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/refill/return", method = POST)
    @ResponseBody
    public void returnFromWork(
            @RequestParam Integer id,
            Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        refillService.returnFromWorkRefillRequest(id, requesterAdminId);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/refill/decline", method = POST)
    @ResponseBody
    public void decline(
            @RequestParam Integer id,
            @RequestParam String comment,
            Principal principal, HttpServletRequest request) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        refillService.declineRefillRequest(id, requesterAdminId, comment);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/refill/accept", method = POST)
    @ResponseBody
    public void accept(@RequestBody RefillRequestAcceptEntryParamsDto acceptDto, Principal principal) {
        Integer requesterAdminId = userService.getIdByEmail(principal.getName());
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .requestId(acceptDto.getRequestId())
                .amount(acceptDto.getAmount())
                .requesterAdminId(requesterAdminId)
                .remark(acceptDto.getRemark())
                .merchantTransactionId(acceptDto.getMerchantTxId())
                .build();
        refillService.acceptRefillRequest(requestAcceptDto);
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

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({
            NotEnoughUserWalletMoneyException.class,
    })
    @ResponseBody
    public ErrorInfo NotAcceptableExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({InvalidAccountException.class})
    @ResponseBody
    public ErrorInfo ForbiddenExceptionHandler(HttpServletRequest req, InvalidAccountException exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception, exception.getReason());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo OtherErrorsHandler(HttpServletRequest req, Exception exception) {
        log.error(ExceptionUtils.getStackTrace(exception));
        if (exception instanceof MerchantException) {
            return new ErrorInfo(req.getRequestURL(), exception,
                    messageSource.getMessage(((MerchantException) (exception)).getReason(), null, localeResolver.resolveLocale(req)));
        }
        return new ErrorInfo(req.getRequestURL(), exception);
    }

}

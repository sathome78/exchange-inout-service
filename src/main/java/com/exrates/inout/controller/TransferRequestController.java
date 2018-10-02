package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.VoucherFilterData;
import com.exrates.inout.domain.enums.NotificationMessageEventEnum;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.enums.invoice.TransferStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.exceptions.entity.ErrorInfo;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import com.exrates.inout.util.CharUtils;
import com.exrates.inout.util.RateLimitService;
import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.OperationType.USER_TRANSFER;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.PRESENT_VOUCHER;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
public class TransferRequestController {

    private static final Logger log = LogManager.getLogger("transfer");

    private final MessageSource messageSource;
    private final TransferService transferService;
    private final UserService userService;
    private final MerchantService merchantService;
    private final InputOutputService inputOutputService;
    private final LocaleResolver localeResolver;
    private final RateLimitService rateLimitService;
    private final CurrencyService currencyService;
    private final SecureService secureServiceImpl;

    private final static String transferRequestCreateDto = "transferRequestCreateDto";

    @Autowired
    public TransferRequestController(MessageSource messageSource, TransferService transferService,
                                     UserService userService, MerchantService merchantService,
                                     InputOutputService inputOutputService, LocaleResolver localeResolver,
                                     RateLimitService rateLimitService, CurrencyService currencyService,
                                     SecureService secureServiceImpl) {
        this.messageSource = messageSource;
        this.transferService = transferService;
        this.userService = userService;
        this.merchantService = merchantService;
        this.inputOutputService = inputOutputService;
        this.localeResolver = localeResolver;
        this.rateLimitService = rateLimitService;
        this.currencyService = currencyService;
        this.secureServiceImpl = secureServiceImpl;
    }


    @RequestMapping(value = "/transfer/request/create", method = POST)
    @ResponseBody
    public Map<String, Object> createTransferRequest(
            @RequestBody TransferRequestParamsDto requestParamsDto,
            Principal principal,
            HttpServletRequest servletRequest){
        Locale locale = localeResolver.resolveLocale(servletRequest);
        if (requestParamsDto.getOperationType() != USER_TRANSFER) {
            throw new IllegalOperationTypeException(requestParamsDto.getOperationType().name());
        }
        if (requestParamsDto.getRecipient() != null && CharUtils.isCyrillic(requestParamsDto.getRecipient())) {
            throw new IllegalArgumentException(messageSource.getMessage(
                    "message.only.latin.symblos", null, locale));
        }
        TransferStatusEnum beginStatus = (TransferStatusEnum) TransferStatusEnum.getBeginState();
        Payment payment = new Payment(requestParamsDto.getOperationType());
        payment.setCurrency(requestParamsDto.getCurrency());
        payment.setMerchant(requestParamsDto.getMerchant());
        payment.setSum(requestParamsDto.getSum() == null ? 0 : requestParamsDto.getSum().doubleValue());
        payment.setRecipient(requestParamsDto.getRecipient());
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, principal.getName(), locale)
                .orElseThrow(InvalidAmountException::new);
        TransferRequestCreateDto transferRequest = new TransferRequestCreateDto(requestParamsDto, creditsOperation, beginStatus, locale);
        try {
            secureServiceImpl.checkEventAdditionalPin(servletRequest, principal.getName(),
                    NotificationMessageEventEnum.TRANSFER, getAmountWithCurrency(transferRequest));
        } catch (PinCodeCheckNeedException e) {
            servletRequest.getSession().setAttribute(transferRequestCreateDto, transferRequest);
            throw e;
        }
        return transferService.createTransferRequest(transferRequest);
    }

    @RequestMapping(value = "/transfer/request/checking", method = POST)
    @ResponseBody
    public void checkingTransferReception(
            @RequestParam String recipient,
            Principal principal, Locale locale,
            HttpServletRequest servletRequest) {
        User user = userService.findByEmail(principal.getName());
        if (user.getNickname().equals(recipient) || user.getEmail().equals(recipient)) {
            throw new InvalidNicknameException(messageSource
                    .getMessage("transfer.selfNickname", null, locale));
        }
    }

    private String getAmountWithCurrency(TransferRequestCreateDto dto) {
        return String.join("", dto.getAmount().stripTrailingZeros().toPlainString(), " ", dto.getCurrencyName());
    }

    @RequestMapping(value = "/transfer/request/pin", method = POST)
    @ResponseBody
    public Map<String, Object> withdrawRequestCheckPin(
            @RequestParam String pin, Locale locale, HttpServletRequest request, Principal principal) {
        Object object = request.getSession().getAttribute(transferRequestCreateDto);
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(pin.length() > 2 && pin.length() < 15);
        if (userService.checkPin(principal.getName(), pin, NotificationMessageEventEnum.TRANSFER)) {
            request.getSession().removeAttribute(transferRequestCreateDto);
            return transferService.createTransferRequest((TransferRequestCreateDto) object);
        } else {
            PinDto res = secureServiceImpl.resendEventPin(request, principal.getName(),
                    NotificationMessageEventEnum.TRANSFER, getAmountWithCurrency((TransferRequestCreateDto) object));
            throw new IncorrectPinException(res);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/transfer/accept", method = POST, produces = "application/json; charset=utf-8")
    public String acceptTransfer(String code, Principal principal, HttpServletRequest request) {
        log.debug("code {}", code);
        if (!rateLimitService.checkLimitsExceed(principal.getName())) {
            throw new RequestsLimitExceedException();
        }
        InvoiceActionTypeEnum action = PRESENT_VOUCHER;
        List<InvoiceStatus> requiredStatus = TransferStatusEnum.getAvailableForActionStatusesList(action);
        if (requiredStatus.size() > 1) {
            throw new RuntimeException("voucher processing error");
        }
        Optional<TransferRequestFlatDto> dto = transferService
                .getByHashAndStatus(code, requiredStatus.get(0).getCode(), true);
        if (!dto.isPresent() || !transferService.checkRequest(dto.get(), principal.getName())) {
            rateLimitService.registerRequest(principal.getName());
            throw new InvoiceNotFoundException(messageSource.getMessage(
                    "voucher.invoice.not.found", null, localeResolver.resolveLocale(request)));
        }
        Locale locale = localeResolver.resolveLocale(request);
        TransferRequestFlatDto flatDto = dto.get();
        flatDto.setInitiatorEmail(principal.getName());
        TransferDto resDto = transferService.performTransfer(flatDto, locale, action);
        JsonObject result = new JsonObject();
        result.addProperty("result", messageSource.getMessage("message.receive.voucher",
                new String[]{BigDecimalProcessing.formatLocaleFixedDecimal(resDto.getNotyAmount(), locale, 4),
                        currencyService.getCurrencyName(flatDto.getCurrencyId())}, localeResolver.resolveLocale(request)));
        return result.toString();
    }

    @RequestMapping(value = "/transfer/request/hash", method = POST)
    @ResponseBody
    public String getHashForUser(@RequestParam Integer id, Principal principal) {
        return transferService.getHash(id, principal);
    }

    @RequestMapping(value = "/transfer/request/revoke", method = POST)
    @ResponseBody
    public void revokeVoucherByUser(@RequestParam Integer id, Principal principal) {
        transferService.revokeByUser(id, principal);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/transfer/request/revoke", method = POST)
    @ResponseBody
    public void revokeVoucherByAdmin(@RequestParam Integer id, Principal principal) {
        transferService.revokeByAdmin(id, principal);
    }

    @GetMapping(value = "/2a8fy7b07dxe44/transfer/request/info")
    public TransferRequestFlatDto getInfoTransfer(@RequestParam Integer id) {
        return transferService.getFlatById(id);
    }

    @GetMapping(value = "/2a8fy7b07dxe44/withdrawal/vouchers")
    public ModelAndView vouchers(Principal principal) {
        final Map<String, Object> params = new HashMap<>();
        List<UserCurrencyOperationPermissionDto> permittedCurrencies = currencyService.getCurrencyOperationPermittedForWithdraw(principal.getName())
                .stream().filter(dto -> dto.getInvoiceOperationPermission() != InvoiceOperationPermission.NONE).collect(Collectors.toList());
        params.put("currencies", permittedCurrencies);
        if (!permittedCurrencies.isEmpty()) {
            List<Integer> currencyList = permittedCurrencies.stream()
                    .map(UserCurrencyOperationPermissionDto::getCurrencyId)
                    .collect(Collectors.toList());
            List<Merchant> merchants = merchantService.getAllUnblockedForOperationTypeByCurrencies(currencyList, OperationType.USER_TRANSFER)
                    .stream()
                    .map(item -> new Merchant(item.getMerchantId(), item.getName(), item.getDescription()))
                    .distinct().collect(Collectors.toList());
            params.put("merchants", merchants);
        }
        List<TransferStatusEnum> statuses = new ArrayList<>(Arrays.asList(TransferStatusEnum.values()));
        params.put("statuses", statuses);
        return new ModelAndView("admin/vouchers", params);
    }

    @GetMapping(value = "/2a8fy7b07dxe44/transfer/requests")
    public DataTable<List<VoucherAdminTableDto>> getAllTransfers(VoucherFilterData filterData,
                                                                 @RequestParam Map<String, String> params,
                                                                 Principal principal,
                                                                 Locale locale) {
        DataTableParams dataTableParams = DataTableParams.resolveParamsFromRequest(params);
        filterData.initFilterItems();
        return transferService.getAdminVouchersList(dataTableParams, filterData, principal.getName(), locale);
    }

    @GetMapping(value = "/transfer/commission")
    public Map<String, String> getCommissions(
            @RequestParam("amount") BigDecimal amount,
            @RequestParam("currency") Integer currencyId,
            @RequestParam("merchant") Integer merchant,
            Principal principal, Locale locale) {
        Integer userId = userService.getIdByEmail(principal.getName());
        return transferService.correctAmountAndCalculateCommissionPreliminarily(userId, amount, currencyId, merchant, locale);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseBody
    public ErrorInfo NotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    @ExceptionHandler(RequestsLimitExceedException.class)
    @ResponseBody
    public ErrorInfo RequestsLimitExceedExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception, messageSource.getMessage("voucher.request.limit.exceed", null, localeResolver.resolveLocale(req)));
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
        return new ErrorInfo(req.getRequestURL(), exception, messageSource
                .getMessage("merchants.notEnoughWalletMoney", null, localeResolver.resolveLocale(req)));
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler({IncorrectPinException.class})
    @ResponseBody
    public PinDto incorrectPinExceptionHandler(HttpServletRequest req, HttpServletResponse response, Exception exception) {
        IncorrectPinException ex = (IncorrectPinException) exception;
        return ex.getDto();
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @ExceptionHandler({PinCodeCheckNeedException.class})
    @ResponseBody
    public ErrorInfo pinCodeCheckNeedExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ErrorInfo(req.getRequestURL(), exception, exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo OtherErrorsHandler(HttpServletRequest req, Exception exception) {
        log.error(ExceptionUtils.getStackTrace(exception));
        return new ErrorInfo(req.getRequestURL(), exception);
    }

}

package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.enums.MerchantApiResponseType;
import com.exrates.inout.domain.enums.MerchantProcessType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.invoice.*;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.other.WithdrawData;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.exceptions.entity.ApiError;
import com.exrates.inout.exceptions.entity.ErrorCode;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import com.exrates.inout.util.RateLimitService;
import com.exrates.inout.util.RestApiUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.CREATE_BY_USER;
import static com.exrates.inout.exceptions.entity.ErrorCode.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api/payments")
public class MobileInputOutputController {

    private static final Logger LOGGER = LogManager.getLogger("mobileAPI");


    private final UserService userService;

    private final MerchantService merchantService;

    private final CommissionService commissionService;

    private final InputOutputService inputOutputService;

    private final MessageSource messageSource;

    private final WithdrawService withdrawService;

    private final RefillService refillService;

    private final TransferService transferService;

    private final RateLimitService rateLimitService;

    private final CurrencyService currencyService;

    @Autowired
    public MobileInputOutputController(CommissionService commissionService, UserService userService, MerchantService merchantService, InputOutputService inputOutputService, MessageSource messageSource, WithdrawService withdrawService, RefillService refillService, TransferService transferService, RateLimitService rateLimitService, CurrencyService currencyService) {
        this.commissionService = commissionService;
        this.userService = userService;
        this.merchantService = merchantService;
        this.inputOutputService = inputOutputService;
        this.messageSource = messageSource;
        this.withdrawService = withdrawService;
        this.refillService = refillService;
        this.transferService = transferService;
        this.rateLimitService = rateLimitService;
        this.currencyService = currencyService;
    }


    @RequestMapping(value = "/merchants", method = GET)
    public List<MerchantCurrencyApiDto> findAllMerchantCurrencies(@RequestParam(required = false) Integer currencyId) {
        return merchantService.findNonTransferMerchantCurrencies(currencyId);
    }

    @RequestMapping(value = "/transferMerchants", method = GET)
    public List<TransferMerchantApiDto> findAllTransferMerchantCurrencies() {
        return merchantService.findTransferMerchants();
    }

    @RequestMapping(value = "/dynamicCommission", method = GET)
    public Double retrieveDynamicCommissionValue(@RequestParam("amount") BigDecimal amount,
                                                 @RequestParam("currency") Integer currencyId,
                                                 @RequestParam("merchant") Integer merchantId,
                                                 @RequestParam(value = "memo", required = false) String memo) {
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);

        Integer userId = userService.getIdByEmail(userEmail);
        if (!StringUtils.isEmpty(memo)) {
            merchantService.checkDestinationTag(merchantId, memo);
        }
        try {
            Map<String, String> result = withdrawService.correctAmountAndCalculateCommissionPreliminarily(userId,
                    amount, currencyId, merchantId, userLocale, memo);
            String merchantCommissionAmountString = result.get("merchantCommissionAmount");
            BigDecimal merchantCommissionAmount = merchantCommissionAmountString == null ? BigDecimal.ZERO : new BigDecimal(merchantCommissionAmountString);
            return merchantCommissionAmount.doubleValue();
        } catch (InvalidAmountException e) {
            throw new CommissionExceedingAmountException(e.getMessage());
        }
    }

    @RequestMapping(value = "/transfer/accept", method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, String> acceptVoucher(@RequestBody Map<String, String> params) {
        String code = RestApiUtils.retrieveParamFormBody(params, "code", true);
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        if (!rateLimitService.checkLimitsExceed(userEmail)) {
            throw new RequestsLimitExceedException();
        }
        InvoiceActionTypeEnum action = InvoiceActionTypeEnum.PRESENT_VOUCHER;
        List<InvoiceStatus> requiredStatus = TransferStatusEnum.getAvailableForActionStatusesList(action);
        if (requiredStatus.size() > 1) {
            throw new RuntimeException("voucher processing error");
        }
        Optional<TransferRequestFlatDto> dto = transferService
                .getByHashAndStatus(code, requiredStatus.get(0).getCode(), true);
        if (!dto.isPresent() || !transferService.checkRequest(dto.get(), userEmail)) {
            rateLimitService.registerRequest(userEmail);
            throw new VoucherNotFoundException(messageSource.getMessage(
                    "voucher.invoice.not.found", null, userLocale));
        }
        TransferRequestFlatDto flatDto = dto.get();
        flatDto.setInitiatorEmail(userEmail);
        transferService.performTransfer(flatDto, userLocale, action);
        return Collections.singletonMap("message", messageSource.getMessage("transfer.accept.success", new Object[]{BigDecimalProcessing.formatLocaleFixedSignificant(flatDto.getAmount(),
                userLocale, 2) + " " + currencyService.getCurrencyName(flatDto.getCurrencyId())}, userLocale));
    }

    @RequestMapping(value = "/preparePayment", method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public MerchantInputResponseDto preparePayment(@RequestBody @Valid RefillRequestParamsDto requestParamsDto, HttpServletRequest request) {
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        if (!refillService.checkInputRequestsLimit(requestParamsDto.getCurrency(), userEmail)) {
            throw new InputRequestLimitExceededException(messageSource.getMessage("merchants.InputRequestsLimit", null, userLocale));
        }
        Merchant merchant = merchantService.findById(requestParamsDto.getMerchant());
        MerchantCurrency merchantCurrency = merchantService.findByMerchantAndCurrency(merchant.getId(), requestParamsDto.getCurrency())
                .orElseThrow(MerchantInternalException::new);
        MerchantInputResponseDto responseDto = new MerchantInputResponseDto();
        if (merchant.getProcessType() == MerchantProcessType.CRYPTO || merchant.getProcessType() == MerchantProcessType.INVOICE) {
            responseDto.setType(MerchantApiResponseType.NOTIFY);
            if (requestParamsDto.getRecipientBankId() != null && requestParamsDto.getAddress() == null) {
                InvoiceBank bank = refillService.findInvoiceBankById(requestParamsDto.getRecipientBankId()).orElseThrow(InvoiceBankNotFoundException::new);
                requestParamsDto.setAddress(bank.getAccountNumber());
                requestParamsDto.setRecipientBankName(bank.getName());
            }
            RefillRequestCreateDto refillRequest = prepareRefillRequest(requestParamsDto, userEmail, userLocale);
            Map<String, Object> result = null;
            // TODO add last address retrieval method for crypto
            try {
                result = refillService.createRefillRequest(refillRequest);
                refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(Collections.singletonList(merchantCurrency), userEmail);

            } catch (RefillRequestGeneratingAdditionalAddressNotAvailableException e) {
                refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(Collections.singletonList(merchantCurrency), userEmail);
                Map<String, String> params = new HashMap<String, String>() {{
                    put("message", refillService.getPaymentMessageForTag(merchant.getServiceBeanName(), merchantCurrency.getAddress(), userLocale));
                }};
                result = new HashMap<String, Object>() {{
                    put("params", params);
                }};
            }
            Map<String, String> params = (Map<String, String>) result.get("params");
            String message = (String) result.get("message");
            if (message == null) {
                message = params.get("message");
            }
            if (message != null) {
                message = message.replaceAll("<button.*>", "").replaceAll("<.*?>", "");
            }
            responseDto.setData(message);
            if (merchantCurrency.getAdditionalTagForWithdrawAddressIsUsed()) {
                responseDto.setQr(merchantCurrency.getMainAddress());
                responseDto.setWalletNumber(merchantCurrency.getMainAddress());
                responseDto.setAdditionalTag(merchantCurrency.getAddress());
            } else {
                responseDto.setQr(params.get("qr"));
                responseDto.setWalletNumber(merchant.getProcessType() == MerchantProcessType.CRYPTO ? params.get("address") : params.get("walletNumber"));
            }

        } else {
            responseDto.setType(MerchantApiResponseType.REDIRECT);
            String rootUrl = String.join("", request.getScheme(), "://", request.getServerName(), ":",
                    String.valueOf(request.getServerPort()), "/api/payments/merchantRedirect?");
            String params = new HashMap<String, Object>() {{
                put("currencyId", requestParamsDto.getCurrency());
                put("merchantId", requestParamsDto.getMerchant());
                put("amount", requestParamsDto.getSum());
            }}.entrySet().stream().map((entry -> entry.getKey() + "=" + entry.getValue())).collect(Collectors.joining("&"));
            responseDto.setData(rootUrl + params);
        }

        return responseDto;
    }

    private RefillRequestCreateDto prepareRefillRequest(@RequestBody @Valid RefillRequestParamsDto requestParamsDto, String userEmail, Locale userLocale) {
        RefillStatusEnum beginStatus = (RefillStatusEnum) RefillStatusEnum.X_STATE.nextState(CREATE_BY_USER);
        Payment payment = new Payment(INPUT);
        payment.setCurrency(requestParamsDto.getCurrency());
        payment.setMerchant(requestParamsDto.getMerchant());
        payment.setSum(requestParamsDto.getSum() == null ? 0 : requestParamsDto.getSum().doubleValue());
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userEmail, userLocale)
                .orElseThrow(InvalidAmountException::new);
        return new RefillRequestCreateDto(requestParamsDto, creditsOperation, beginStatus, userLocale);
    }


    @RequestMapping(value = "/invoice/confirm", method = POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> confirmInvoice(@Valid InvoiceConfirmData invoiceConfirmData) {
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        refillService.confirmRefillRequest(invoiceConfirmData, userLocale);
        return new ResponseEntity<>(OK);
    }

    @RequestMapping(value = "/invoice/revoke", method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Void> revokeInvoice(@RequestBody Map<String, String> params) {
        String invoiceIdString = RestApiUtils.retrieveParamFormBody(params, "invoiceId", true);
        Integer invoiceId = Integer.parseInt(invoiceIdString);
        InvoiceConfirmData invoiceConfirmData = new InvoiceConfirmData();
        invoiceConfirmData.setInvoiceId(invoiceId);
        refillService.revokeRefillRequest(invoiceId);
        return new ResponseEntity<>(OK);
    }


    @RequestMapping(value = "/invoice/banks", method = GET)
    public List<InvoiceBank> getBanksByCurrency(@RequestParam Integer currencyId) {
        return Collections.singletonList(InvoiceBank.getUnavilableInvoice(currencyId));
    }

    @RequestMapping(value = "/invoice/clientBanks", method = GET)
    public List<ClientBank> getClientBanksByCurrency(@RequestParam Integer currencyId) {
        return withdrawService.findClientBanksForCurrency(currencyId);
    }


    @RequestMapping(value = "/invoice/details", method = GET)
    public RefillRequestDetailsDto findInvoiceRequestDetails(@RequestParam Integer invoiceId, HttpServletRequest request) {
        RefillRequestFlatDto refillRequest = refillService.getFlatById(invoiceId);
        BigDecimal commissionAmount = commissionService.calculateCommissionForRefillAmount(refillRequest.getAmount(), refillRequest.getCommissionId());
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/rest";
        return new RefillRequestDetailsDto(refillRequest, commissionAmount, baseUrl);
    }

    @RequestMapping(value = "/invoice/withdraw", method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Map<String, String>> withdrawInvoice(@RequestBody @Valid WithdrawRequestParamsDto requestParamsDto) {
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        if (!withdrawService.checkOutputRequestsLimit(requestParamsDto.getCurrency(), userEmail)) {
            throw new RequestLimitExceededException(messageSource.getMessage("merchants.OutputRequestsLimit", null, userLocale));
        }
        Payment payment = new Payment();
        payment.setSum(requestParamsDto.getSum().doubleValue());
        payment.setCurrency(requestParamsDto.getCurrency());
        payment.setMerchant(merchantService.findByName("Invoice").getId());
        payment.setOperationType(OperationType.OUTPUT);
        payment.setDestination(requestParamsDto.getWalletNumber());

        WithdrawData withdrawData = new WithdrawData();
        withdrawData.setRecipientBankName(requestParamsDto.getRecipientBankName());
        withdrawData.setRecipientBankCode(requestParamsDto.getRecipientBankCode());
        withdrawData.setUserFullName(requestParamsDto.getUserFullName());
        withdrawData.setRemark(requestParamsDto.getRemark());


        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userEmail, userLocale).orElseThrow(InvalidAmountException::new);
        WithdrawStatusEnum beginStatus = (WithdrawStatusEnum) WithdrawStatusEnum.getBeginState();
        WithdrawRequestCreateDto withdrawRequestCreateDto = new WithdrawRequestCreateDto(requestParamsDto, creditsOperation, beginStatus);
        Map<String, String> response = withdrawService.createWithdrawalRequest(withdrawRequestCreateDto, userLocale);

        return new ResponseEntity<>(response, OK);
    }

    @RequestMapping(value = "/withdraw/revoke", method = POST)
    @ResponseBody
    public ResponseEntity<Void> revokeWithdrawRequest(@RequestBody Map<String, String> params) {
        Integer id = Integer.parseInt(RestApiUtils.retrieveParamFormBody(params, "invoiceId", true));
        withdrawService.revokeWithdrawalRequest(id);
        return new ResponseEntity<>(OK);
    }


    @RequestMapping(value = "/merchantRedirect", method = GET)
    public ModelAndView getMerchantRedirectPage(@RequestParam Integer currencyId, @RequestParam Integer merchantId,
                                                @RequestParam BigDecimal amount, @RequestParam String token) {
        ModelAndView modelAndView = new ModelAndView("merchantApiInput");
        modelAndView.addObject("currency", currencyId);
        modelAndView.addObject("merchant", merchantId);
        modelAndView.addObject("amount", amount);
        modelAndView.addObject("authToken", token);
        modelAndView.addObject("operationType", OperationType.INPUT);
        return modelAndView;
    }

    @RequestMapping(value = "/preparePostPayment", method = POST)
    public Map<String, Object> preparePostPayment(@Valid RefillRequestParamsDto requestParamsDto) {
        LOGGER.debug(requestParamsDto);
        Payment payment = new Payment(INPUT);
        payment.setCurrency(requestParamsDto.getCurrency());
        payment.setMerchant(requestParamsDto.getMerchant());
        payment.setSum(requestParamsDto.getSum() == null ? 0 : requestParamsDto.getSum().doubleValue());
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, userEmail, userLocale)
                .orElseThrow(InvalidAmountException::new);
        RefillStatusEnum beginStatus = (RefillStatusEnum) RefillStatusEnum.X_STATE.nextState(CREATE_BY_USER);
        RefillRequestCreateDto refillRequest = new RefillRequestCreateDto(requestParamsDto, creditsOperation, beginStatus, userLocale);
        return refillService.createRefillRequest(refillRequest);
    }

    private String getAuthenticatedUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @RequestMapping(value = "/lastAddress", method = GET)
    public CryptoAddressDto getLastUsedAddressForMerchantAndCurrency(@RequestParam Integer currencyId, @RequestParam Integer merchantId) {
        String userEmail = getAuthenticatedUserEmail();
        Locale userLocale = userService.getUserLocaleForMobile(userEmail);
        List<MerchantCurrency> merchantCurrencyData = merchantService.getAllUnblockedForOperationTypeByCurrencies(
                Collections.singletonList(currencyId), OperationType.INPUT).stream()
                .filter(item -> item.getMerchantId() == merchantId).collect(Collectors.toList());
        refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(merchantCurrencyData, userEmail);
        CryptoAddressDto result = merchantCurrencyData.stream().map(CryptoAddressDto::new).
                findFirst().orElseThrow(() -> new MerchantNotFoundException(String.valueOf(merchantId)));
        if (StringUtils.isEmpty(result.getAddress())) {
            RefillRequestParamsDto requestParamsDto = new RefillRequestParamsDto();
            requestParamsDto.setCurrency(currencyId);
            requestParamsDto.setMerchant(merchantId);
            requestParamsDto.setSum(BigDecimal.ZERO);
            RefillRequestCreateDto refillRequest = prepareRefillRequest(requestParamsDto, userEmail, userLocale);
            refillService.createRefillRequest(refillRequest);
            refillService.retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(merchantCurrencyData, userEmail);
            result = merchantCurrencyData.stream().map(CryptoAddressDto::new).
                    findFirst().orElseThrow(() -> new MerchantNotFoundException(String.valueOf(merchantId)));
        }
        return result;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ApiError httpMessageNotReadableExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(REQUEST_NOT_READABLE, req.getRequestURL(), exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public ApiError missingServletRequestParameterHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.MISSING_REQUIRED_PARAM, req.getRequestURL(), exception);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public ApiError mismatchArgumentsErrorHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.INVALID_PARAM_VALUE, req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidAmountException.class)
    public ApiError invalidAmountExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(INVALID_PAYMENT_AMOUNT, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler({NotEnoughMoneyException.class, NotEnoughUserWalletMoneyException.class})
    public ApiError notEnoughMoneyExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(INSUFFICIENT_FUNDS, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({InvoiceNotFoundException.class, InvoiceNotFoundException.class})
    @ResponseBody
    public ApiError invoiceNotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.INVOICE_NOT_FOUND, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler({UserNotFoundException.class, UserNotFoundException.class})
    @ResponseBody
    public ApiError userNotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.USER_NOT_FOUND, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(VoucherNotFoundException.class)
    @ResponseBody
    public ApiError VoucherNotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.VOUCHER_NOT_FOUND, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(InvalidNicknameException.class)
    @ResponseBody
    public ApiError invalidNicknameExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.SELF_TRANSFER_NOT_ALLOWED, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(MerchantCurrencyBlockedException.class)
    @ResponseBody
    public ApiError merchantCurrencyBlockedExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.BLOCKED_CURRENCY_FOR_MERCHANT, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(InputRequestLimitExceededException.class)
    @ResponseBody
    public ApiError inputRequestLimitExceededExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.INPUT_REQUEST_LIMIT_EXCEEDED, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(RequestLimitExceededException.class)
    @ResponseBody
    public ApiError outputRequestLimitExceededExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.OUTPUT_REQUEST_LIMIT_EXCEEDED, req.getRequestURL(), exception);
    }

    @ResponseStatus(NOT_ACCEPTABLE)
    @ExceptionHandler(CommissionExceedingAmountException.class)
    @ResponseBody
    public ApiError commissionExceedingAmountExceptionHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.COMMISSION_EXCEEDS_AMOUNT, req.getRequestURL(), exception);
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ApiError OtherErrorsHandler(HttpServletRequest req, Exception exception) {
        return new ApiError(ErrorCode.INTERNAL_SERVER_ERROR, req.getRequestURL(), exception);
    }


}

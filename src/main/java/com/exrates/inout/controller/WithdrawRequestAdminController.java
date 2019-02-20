package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.UserCurrencyOperationPermissionDto;
import com.exrates.inout.domain.dto.WithdrawFilterData;
import com.exrates.inout.domain.dto.WithdrawRequestTableReportDto;
import com.exrates.inout.domain.dto.WithdrawRequestsAdminTableDto;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.WithdrawRequestTableViewTypeEnum;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.exceptions.entity.ErrorInfo;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.WithdrawService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
public class WithdrawRequestAdminController {

    private static final Logger log = LogManager.getLogger("withdraw");

    private final WithdrawService withdrawService;

    private final MerchantService merchantService;

    private final CurrencyService currencyService;

    @Autowired
    public WithdrawRequestAdminController(WithdrawService withdrawService, MerchantService merchantService, CurrencyService currencyService) {
        this.withdrawService = withdrawService;
        this.merchantService = merchantService;
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdrawal")
    public ModelAndView withdrawalRequests(Principal principal) {
        final Map<String, Object> params = new HashMap<>();
        List<UserCurrencyOperationPermissionDto> permittedCurrencies = currencyService.getCurrencyOperationPermittedForWithdraw(principal.getName())
                .stream().filter(dto -> dto.getInvoiceOperationPermission() != InvoiceOperationPermission.NONE)
                .sorted(Comparator.comparing(UserCurrencyOperationPermissionDto::getCurrencyName))
                .collect(Collectors.toList());
        params.put("currencies", permittedCurrencies);
        if (!permittedCurrencies.isEmpty()) {
            List<Integer> currencyList = permittedCurrencies.stream()
                    .map(UserCurrencyOperationPermissionDto::getCurrencyId)
                    .collect(Collectors.toList());
            List<Merchant> merchants = merchantService.getAllUnblockedForOperationTypeByCurrencies(currencyList, OperationType.OUTPUT)
                    .stream()
                    .map(item -> new Merchant(item.getMerchantId(), item.getName(), item.getDescription()))
                    .distinct().sorted(Comparator.comparing(Merchant::getName)).collect(Collectors.toList());
            params.put("merchants", merchants);
        }
        return new ModelAndView("withdrawalRequests", params);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdrawRequests", method = GET)
    @ResponseBody
    public DataTable<List<WithdrawRequestsAdminTableDto>> findRequestByStatus(
            @RequestParam("viewType") String viewTypeName,
            WithdrawFilterData withdrawFilterData,
            @RequestParam Map<String, String> params,
            Principal principal,
            Locale locale) {
        WithdrawRequestTableViewTypeEnum viewTypeEnum = WithdrawRequestTableViewTypeEnum.convert(viewTypeName);
        List<Integer> statusList = viewTypeEnum.getWithdrawStatusList().stream().map(WithdrawStatusEnum::getCode).collect(Collectors.toList());
        DataTableParams dataTableParams = DataTableParams.resolveParamsFromRequest(params);
        withdrawFilterData.initFilterItems();
        return withdrawService.getWithdrawRequestByStatusList(statusList, dataTableParams, withdrawFilterData, principal.getName(), locale);
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/withdrawRequests/report", method = GET)
    @ResponseBody
    public String getRequestsReportByStatus(
            @RequestParam("viewType") String viewTypeName,
            WithdrawFilterData withdrawFilterData,
            Principal principal,
            Locale locale) {
        WithdrawRequestTableViewTypeEnum viewTypeEnum = WithdrawRequestTableViewTypeEnum.convert(viewTypeName);
        List<Integer> statusList = viewTypeEnum.getWithdrawStatusList().stream().map(WithdrawStatusEnum::getCode).collect(Collectors.toList());
        DataTableParams dataTableParams = DataTableParams.defaultParams();
        withdrawFilterData.initFilterItems();
        return withdrawService.getWithdrawRequestByStatusList(statusList, dataTableParams, withdrawFilterData, principal.getName(), locale)
                .getData().stream().map(dto -> new WithdrawRequestTableReportDto(dto).toString())
                .collect(Collectors.joining("", WithdrawRequestTableReportDto.getTitle(), ""));
    }


    @RequestMapping(value = "/2a8fy7b07dxe44/withdraw/info", method = GET)
    @ResponseBody
    public WithdrawRequestsAdminTableDto getInfo(
            @RequestParam Integer id,
            Principal principal) {
        String requesterAdmin = principal.getName();
        return withdrawService.getWithdrawRequestById(id, requesterAdmin);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(InvoiceNotFoundException.class)
    @ResponseBody
    public ErrorInfo NotFoundExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler({InvoiceActionIsProhibitedForCurrencyPermissionOperationException.class,
            InvoiceActionIsProhibitedForNotHolderException.class
    })

    @ResponseBody
    public ErrorInfo ForbiddenExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler({NotEnoughUserWalletMoneyException.class, RequestLimitExceededException.class})
    @ResponseBody
    public ErrorInfo NotAcceptableExceptionHandler(HttpServletRequest req, Exception exception) {
        log.error(exception);
        return new ErrorInfo(req.getRequestURL(), exception);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo OtherErrorsHandler(HttpServletRequest req, Exception exception) {
        log.error(ExceptionUtils.getStackTrace(exception));
        return new ErrorInfo(req.getRequestURL(), exception);
    }

}

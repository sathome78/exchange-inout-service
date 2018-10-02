package com.exrates.inout.service.impl;

import com.exrates.inout.dao.TransferRequestDao;
import com.exrates.inout.domain.dto.TransferDto;
import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.domain.dto.TransferRequestFlatDto;
import com.exrates.inout.domain.dto.VoucherAdminTableDto;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.VoucherFilterData;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.enums.invoice.TransferStatusEnum;
import com.exrates.inout.domain.main.MerchantCurrency;
import com.exrates.inout.domain.main.NotificationEvent;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.domain.main.TransferRequest;
import com.exrates.inout.domain.other.ProfileData;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.service.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.OperationType.USER_TRANSFER;
import static com.exrates.inout.domain.enums.WalletTransferStatus.SUCCESS;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.*;
import static com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection.TRANSFER_VOUCHER;

@Service
public class TransferServiceImpl implements TransferService {

    private static final Logger log = LogManager.getLogger("transfer");

    private final CurrencyService currencyService;

    private final MessageSource messageSource;

    private final TransferRequestDao transferRequestDao;

    private final WalletService walletService;

    private final UserService userService;

    private final NotificationService notificationService;

    private final TransactionDescription transactionDescription;

    private final MerchantServiceContext merchantServiceContext;

    private final CommissionService commissionService;

    private final InputOutputService inputOutputService;

    private final MerchantService merchantService;

    @Autowired
    public TransferServiceImpl(TransferRequestDao transferRequestDao, CurrencyService currencyService, MessageSource messageSource, WalletService walletService, UserService userService, NotificationService notificationService, TransactionDescription transactionDescription, MerchantServiceContext merchantServiceContext, CommissionService commissionService, InputOutputService inputOutputService, MerchantService merchantService) {
        this.transferRequestDao = transferRequestDao;
        this.currencyService = currencyService;
        this.messageSource = messageSource;
        this.walletService = walletService;
        this.userService = userService;
        this.notificationService = notificationService;
        this.transactionDescription = transactionDescription;
        this.merchantServiceContext = merchantServiceContext;
        this.commissionService = commissionService;
        this.inputOutputService = inputOutputService;
        this.merchantService = merchantService;
    }

    @Transactional
    public Map<String, Object> createTransferRequest(TransferRequestCreateDto request) {
        ProfileData profileData = new ProfileData(1000);
        try {
            IMerchantService merchantService = merchantServiceContext.getMerchantService(request.getServiceBeanName());
            ITransferable transferMerchantService = (ITransferable) merchantService;
            request.setIsVoucher(transferMerchantService.isVoucher());
            if (transferMerchantService.recipientUserIsNeeded()) {
                checkTransferToSelf(request.getUserId(), request.getRecipientId(), request.getLocale());
            }
            Integer requestId = createTransfer(request);
            request.setId(requestId);
            Map<String, String> data = transferMerchantService.transfer(request);
            request.setHash(data.get("hash"));
            transferRequestDao.setHashById(requestId, data);
            /**/
            String notification = null;
            try {
                notification = sendTransferNotification(
                        new TransferRequest(request),
                        request.getMerchantDescription(),
                        request.getLocale());
            } catch (final MailException e) {
                log.error(e);
            }
            profileData.setTime2();
            BigDecimal newAmount = walletService.getWalletABalance(request.getUserWalletId());
            String currency = request.getCurrencyName();
            String balance = currency + " " + currencyService.amountToString(newAmount, currency);
            Map<String, Object> result = new HashMap<>();
            result.put("message", notification);
            result.put("balance", balance);
            result.put("hash", request.getHash());
            profileData.setTime3();
            return result;
        } finally {
            profileData.checkAndLog("slow create TransferRequest: " + request + " profile: " + profileData);
        }
    }

    private void checkTransferToSelf(Integer userId, Integer recipientId, Locale locale) {
        if (userId.equals(recipientId)) {
            throw new InvalidNicknameException(messageSource
                    .getMessage("transfer.selfNickname", null, locale));
        }
    }

    @Transactional(rollbackFor = {Exception.class})
    private Integer createTransfer(TransferRequestCreateDto transferRequestCreateDto) {
        TransferStatusEnum currentStatus = TransferStatusEnum.convert(transferRequestCreateDto.getStatusId());
        Boolean isVoucher = transferRequestCreateDto.getIsVoucher();
        InvoiceActionTypeEnum action = currentStatus.getStartAction(isVoucher);
        InvoiceStatus newStatus = currentStatus.nextState(action);
        transferRequestCreateDto.setStatusId(newStatus.getCode());
        int createdTransferRequestId = 0;
        if (walletService.ifEnoughMoney(
                transferRequestCreateDto.getUserWalletId(),
                transferRequestCreateDto.getAmount())) {
            if ((createdTransferRequestId = transferRequestDao.create(transferRequestCreateDto)) > 0) {
                String description = transactionDescription.get(currentStatus, action);
                if (isVoucher) {
                    WalletTransferStatus result = walletService.walletInnerTransfer(
                            transferRequestCreateDto.getUserWalletId(),
                            transferRequestCreateDto.getAmount().negate(),
                            TransactionSourceType.USER_TRANSFER,
                            createdTransferRequestId,
                            description);
                    if (result != SUCCESS) {
                        throw new TransferRequestCreationException(result.toString());
                    }
                } else {
                    walletService.transferCostsToUser(
                            transferRequestCreateDto.getUserId(),
                            transferRequestCreateDto.getUserWalletId(),
                            transferRequestCreateDto.getRecipientId(),
                            transferRequestCreateDto.getAmount(),
                            transferRequestCreateDto.getCommission(),
                            transferRequestCreateDto.getLocale(),
                            createdTransferRequestId);
                }
            }
        } else {
            throw new NotEnoughUserWalletMoneyException(transferRequestCreateDto.toString());
        }
        return createdTransferRequestId;
    }

    @Transactional
    public List<MerchantCurrency> retrieveAdditionalParamsForWithdrawForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies) {
        merchantCurrencies.forEach(e -> {
            IMerchantService merchantService = merchantServiceContext.getMerchantService(e.getMerchantId());
            if (merchantService instanceof ITransferable) {
                e.setRecipientUserIsNeeded(((ITransferable) merchantService).recipientUserIsNeeded());
                e.setProcessType(((ITransferable) merchantService).processType().name());
            }
        });
        return merchantCurrencies;
    }

    @Transactional
    public void revokeByUser(int requestId, Principal principal) {
        TransferRequestFlatDto transferRequest = transferRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new InvoiceNotFoundException(String.format("withdraw request id: %s", requestId)));

        if (principal == null || !getUserEmailByTrnasferId(requestId).equals(principal.getName())) {
            throw new TransferRequestRevokeException();
        }
        TransferStatusEnum currentStatus = transferRequest.getStatus();
        TransferStatusEnum newStatus = (TransferStatusEnum) currentStatus.nextState(REVOKE);
        revokeTransferRequest(transferRequest, REVOKE, newStatus);
    }

    @Transactional
    public void revokeByAdmin(int requestId, Principal principal) {
        TransferRequestFlatDto transferRequest = transferRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new InvoiceNotFoundException(String.format("withdraw request id: %s", requestId)));
        InvoiceOperationPermission permission = userService.getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(
                userService.getIdByEmail(principal.getName()),
                transferRequest.getCurrencyId(),
                TRANSFER_VOUCHER
        );
        if (permission != null && REVOKE_ADMIN.getOperationPermissionOnlyList().contains(permission)) {

        }
        TransferStatusEnum currentStatus = transferRequest.getStatus();
        TransferStatusEnum newStatus = (TransferStatusEnum) currentStatus.nextState(REVOKE, InvoiceActionTypeEnum.InvoiceActionParamsValue.builder()
                .authorisedUserIsHolder(true)
                .permittedOperation(permission)
                .availableForCurrentContext(false).build());
        revokeTransferRequest(transferRequest, REVOKE_ADMIN, newStatus);
    }

    @Transactional
    private void revokeTransferRequest(TransferRequestFlatDto transferRequest, InvoiceActionTypeEnum action, TransferStatusEnum newStatus) {
        transferRequestDao.setStatusById(transferRequest.getId(), newStatus);
        /**/
        Integer userWalletId = walletService.getWalletId(transferRequest.getUserId(), transferRequest.getCurrencyId());
        String description = transactionDescription.get(transferRequest.getStatus(), action);
        WalletTransferStatus result = walletService.walletInnerTransfer(
                userWalletId,
                transferRequest.getAmount(),
                TransactionSourceType.USER_TRANSFER,
                transferRequest.getId(),
                description);
        if (result != SUCCESS) {
            throw new TransferRequestRevokeException(result.toString());
        }
    }

    @Transactional(readOnly = true)
    public TransferRequestFlatDto getFlatById(Integer id) {
        return transferRequestDao.getFlatById(id)
                .orElseThrow(() -> new TransferRequestNotFoundException(id.toString()));
    }

    private String sendTransferNotification(
            TransferRequest transferRequest,
            String merchantDescription,
            Locale locale) {
        final String notification;
        final Object[] messageParams = {
                transferRequest.getId(),
                merchantDescription
        };
        String notificationMessageCode;
        notificationMessageCode = "merchants.transferNotification.".concat(transferRequest.getStatus().name());
        notification = messageSource
                .getMessage(notificationMessageCode, messageParams, locale);
        notificationService.notifyUser(transferRequest.getUserEmail(), NotificationEvent.IN_OUT,
                "merchants.transferNotification.header", notificationMessageCode, messageParams);
        return notification;
    }

    @Transactional(readOnly = true)
    public Map<String, String> correctAmountAndCalculateCommissionPreliminarily(
            Integer userId,
            BigDecimal amount,
            Integer currencyId,
            Integer merchantId,
            Locale locale) {
        OperationType operationType = USER_TRANSFER;
        BigDecimal addition = currencyService.computeRandomizedAddition(currencyId, operationType);
        amount = amount.add(addition);
        merchantService.checkAmountForMinSum(merchantId, currencyId, amount);
        Map<String, String> result = commissionService.computeCommissionAndMapAllToString(userId, amount, operationType, currencyId, merchantId, locale, null);
        result.put("addition", addition.toString());
        return result;
    }

    public Optional<TransferRequestFlatDto> getByHashAndStatus(String code, Integer requiredStatus, boolean block) {
        return transferRequestDao.getFlatByHashAndStatus(code, requiredStatus, block);
    }

    public boolean checkRequest(TransferRequestFlatDto transferRequestFlatDto, String userEmail) {
        ITransferable merchantService = (ITransferable) merchantServiceContext.getMerchantService(transferRequestFlatDto.getMerchantId());
        return !merchantService.recipientUserIsNeeded() || transferRequestFlatDto.getRecipientId().equals(userService.getIdByEmail(userEmail));
    }

    @Transactional
    public TransferDto performTransfer(TransferRequestFlatDto dto, Locale locale, InvoiceActionTypeEnum action) {
        checkTransferToSelf(dto.getUserId(), dto.getRecipientId(), locale);
        IMerchantService merchantService = merchantServiceContext.getMerchantService(dto.getMerchantId());
        if (!(merchantService instanceof ITransferable)) {
            throw new MerchantException("not supported merchant");
        }
        if (((ITransferable) merchantService).isVoucher() && !((ITransferable) merchantService).recipientUserIsNeeded()) {
            dto.setRecipientId(userService.getIdByEmail(dto.getInitiatorEmail()));
            transferRequestDao.setRecipientById(dto.getId(), dto.getRecipientId());
        }
        TransferStatusEnum currentStatus = dto.getStatus();
        TransferStatusEnum newStatus = (TransferStatusEnum) currentStatus.nextState(action);
        if (!newStatus.isEndStatus()) {
            throw new TransferRequestAcceptExeption("invalid new status " + newStatus);
        }
        int walletId = walletService.getWalletIdAndBlock(dto.getUserId(), dto.getCurrencyId());
        WalletTransferStatus result = walletService.walletInnerTransfer(
                walletId,
                dto.getAmount(),
                TransactionSourceType.USER_TRANSFER,
                dto.getId(),
                transactionDescription.get(currentStatus, action));
        if (result != SUCCESS) {
            throw new WithdrawRequestPostException(result.name());
        }
        TransferDto resDto = walletService.transferCostsToUser(walletId, dto.getRecipientId(), dto.getAmount(), dto.getCommissionAmount(), locale, dto.getId());
        transferRequestDao.setStatusById(dto.getId(), newStatus);
        return resDto;
    }

    public String getUserEmailByTrnasferId(int id) {
        return transferRequestDao.getCreatorEmailById(id);
    }

    @Transactional
    public DataTable<List<VoucherAdminTableDto>> getAdminVouchersList(
            DataTableParams dataTableParams,
            VoucherFilterData withdrawFilterData,
            String authorizedUserEmail,
            Locale locale) {
        Integer authorizedUserId = userService.getIdByEmail(authorizedUserEmail);
        PagingData<List<TransferRequestFlatDto>> result = transferRequestDao.getPermittedFlat(
                authorizedUserId,
                dataTableParams,
                withdrawFilterData);
        DataTable<List<VoucherAdminTableDto>> output = new DataTable<>();
        output.setData(result.getData().stream()
                .map(VoucherAdminTableDto::new)
                .peek(e -> e.setButtons(
                        inputOutputService.generateAndGetButtonsSet(
                                e.getStatus(),
                                e.getInvoiceOperationPermission(),
                                true,
                                locale)
                ))
                .collect(Collectors.toList())
        );
        output.setRecordsTotal(result.getTotal());
        output.setRecordsFiltered(result.getFiltered());
        return output;
    }

    public String getHash(Integer id, Principal principal) {
        TransferRequestFlatDto dto = getFlatById(id);
        if (dto == null || !dto.getCreatorEmail().equals(principal.getName())
                || !dto.getStatus().availableForAction(PRESENT_VOUCHER)) {
            throw new InvoiceNotFoundException("");
        }
        return transferRequestDao.getHashById(id);
    }
}

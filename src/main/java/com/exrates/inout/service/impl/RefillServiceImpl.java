package com.exrates.inout.service.impl;

import com.exrates.inout.dao.MerchantDao;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.dto.filterdata.RefillFilterData;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.other.ProfileData;
import com.exrates.inout.domain.other.WalletOperationData;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.ActionType.SUBTRACT;
import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static com.exrates.inout.domain.enums.UserCommentTopicEnum.REFILL_ACCEPTED;
import static com.exrates.inout.domain.enums.UserCommentTopicEnum.REFILL_DECLINE;
import static com.exrates.inout.domain.enums.WalletTransferStatus.SUCCESS;
import static com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum.*;
import static com.exrates.inout.domain.enums.invoice.InvoiceOperationDirection.REFILL;
import static com.exrates.inout.domain.other.WalletOperationData.BalanceType.ACTIVE;

@Service
public class RefillServiceImpl implements RefillService {

    @Value("${invoice.blockNotifyUsers}")
    private Boolean BLOCK_NOTIFYING;

    private static final Logger log = LogManager.getLogger("refill");
    @Autowired
    private MerchantDao merchantDao;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillRequestDao refillRequestDao;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CompanyWalletService companyWalletService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserService userService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private TransactionDescription transactionDescription;
    @Autowired
    private  MerchantServiceContext merchantServiceContext;
    @Autowired
    private  CommissionService commissionService;
    @Autowired
    private  UserFilesService userFilesService;


    @Autowired
    private InputOutputService inputOutputService;

    @Transactional
    public Map<String, Object> createRefillRequest(RefillRequestCreateDto request) {
        ProfileData profileData = new ProfileData(1000);
        Map<String, Object> result = new HashMap<String, Object>() {{ put("params", new HashMap<String, String>()); }};
        try {
            IRefillable merchantService = (IRefillable) merchantServiceContext.getMerchantService(request.getServiceBeanName());
            request.setNeedToCreateRefillRequestRecord(merchantService.needToCreateRefillRequestRecord());
            request.setGenerateAdditionalRefillAddressAvailable(merchantService.generatingAdditionalRefillAddressAvailable());
            request.setStoreSameAddressForParentAndTokens(merchantService.storeSameAddressForParentAndTokens());
            if (merchantService.createdRefillRequestRecordNeeded()) {
                Integer requestId = createRefill(request).orElse(null);
                request.setId(requestId);
            }
            profileData.setTime1();
            merchantService.refill(request).entrySet().forEach(e ->
            {
                if (e.getKey().startsWith("$__")) {
                    result.put(e.getKey().replace("$__", ""), e.getValue());
                } else {
                    ((Map<String, String>) result.get("params")).put(e.getKey(), e.getValue());
                }
            });
            String merchantRequestSign = (String) result.get("sign");
            request.setMerchantRequestSign(merchantRequestSign);
            if (merchantRequestSign != null) {
                refillRequestDao.setMerchantRequestSignById(request.getId(), merchantRequestSign);
            }
            if (((Map<String, String>) result.get("params")).keySet().contains("address")) {
                if (StringUtils.isEmpty(((Map<String, String>) result.get("params")).get("address"))) {
                    throw new RefillRequestExpectedAddressNotDetermineException(request.toString());
                }
                if (!merchantService.generatingAdditionalRefillAddressAvailable()) {
                    Boolean addressIsAlreadyGeneratedForUser = refillRequestDao.findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(
                            request.getMerchantId(),
                            request.getCurrencyId(),
                            request.getUserId()
                    ).isPresent();
                    if (addressIsAlreadyGeneratedForUser) {
                        throw new RefillRequestGeneratingAdditionalAddressNotAvailableException(request.toString());
                    }
                }
            }
            request.setAddress(((Map<String, String>) result.get("params")).get("address"));
            request.setPrivKey(((Map<String, String>) result.get("params")).get("privKey"));
            request.setPubKey(((Map<String, String>) result.get("params")).get("pubKey"));
            request.setBrainPrivKey(((Map<String, String>) result.get("params")).get("brainPrivKey"));
            profileData.setTime2();
            if (request.getId() == null) {
                Integer requestId = createRefill(request).orElse(null);
                request.setId(requestId);
            }
            profileData.setTime3();
      /*if (merchantService.concatAdditionalToMainAddress()) {
        ((Map<String, String>) result.get("params")).put("address", merchantService.getMainAddress().concat(request.getAddress()));
      }*/
        } finally {
            profileData.checkAndLog("slow create RefillRequest: " + request + " profile: " + profileData);
        }
        if (request.getId() != null) {
            try {
                String notification = sendRefillNotificationAfterCreation(
                        request,
                        ((Map<String, String>) result.get("params")).get("message"),
                        request.getLocale());
                result.put("message", notification);
                result.put("requestId", request.getId());
            } catch (MailException e) {
                log.error(e);
            }
        }
        return result;
    }

    @Transactional
    public Optional<String> getAddressByMerchantIdAndCurrencyIdAndUserId(Integer merchantId, Integer currencyId, Integer userId) {
        return refillRequestDao.findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(merchantId, currencyId, userId);
    }

    @Transactional(readOnly = true)
    public Integer getMerchantIdByAddressAndCurrencyAndUser(String address, Integer currencyId, Integer userId) {
        return refillRequestDao.findMerchantIdByAddressAndCurrencyAndUser(address, currencyId, userId);
    }

    @Transactional
    public List<MerchantCurrency> retrieveAddressAndAdditionalParamsForRefillForMerchantCurrencies(List<MerchantCurrency> merchantCurrencies, String userEmail) {
        Integer userId = userService.getIdByEmail(userEmail);
        merchantCurrencies.forEach(e -> {

            e.setAddress(refillRequestDao.findLastValidAddressByMerchantIdAndCurrencyIdAndUserId(e.getMerchantId(), e.getCurrencyId(), userId).orElse(""));
            /**/
            //TODO: Temporary fix
            if (e.getMerchantId() == merchantService.findByName("EDC").getId()) {
                e.setAddress("");
            }
            IRefillable merchantService = (IRefillable) merchantServiceContext.getMerchantService(e.getMerchantId());
            e.setGenerateAdditionalRefillAddressAvailable(merchantService.generatingAdditionalRefillAddressAvailable());
            e.setAdditionalTagForWithdrawAddressIsUsed(((IWithdrawable) merchantService).additionalTagForWithdrawAddressIsUsed());
            if (e.getAdditionalTagForWithdrawAddressIsUsed()) {
                e.setMainAddress(merchantService.getMainAddress());
                e.setAdditionalFieldName(merchantService.additionalRefillFieldName());
            }
            if (!StringUtils.isEmpty(e.getAddress()) && merchantService.concatAdditionalToMainAddress()) {
                e.setAddress(merchantService.getMainAddress().concat(e.getAddress()));
            }
        });
        return merchantCurrencies;
    }

    @Transactional
    public void confirmRefillRequest(InvoiceConfirmData invoiceConfirmData, Locale locale) {
        Integer requestId = invoiceConfirmData.getInvoiceId();
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        RefillStatusEnum currentStatus = refillRequest.getStatus();
        InvoiceActionTypeEnum action = CONFIRM_USER;
        RefillStatusEnum newStatus = (RefillStatusEnum) currentStatus.nextState(action);
        /**/
        MultipartFile receiptScan = invoiceConfirmData.getReceiptScan();
        boolean emptyFile = receiptScan == null || receiptScan.isEmpty();
        if (emptyFile) {
            throw new FileLoadingException(messageSource.getMessage("refill.receiptScan.absent", null, locale));
        }
        if (!userFilesService.checkFileValidity(receiptScan) || receiptScan.getSize() > 1048576L) {
            throw new FileLoadingException(messageSource.getMessage("merchants.errorUploadReceipt", null, locale));
        }
        try {
            String scanPath = userFilesService.saveReceiptScan(refillRequest.getUserId(), refillRequest.getId(), receiptScan);
            invoiceConfirmData.setReceiptScanPath(scanPath);
        } catch (IOException e) {
            throw new FileLoadingException(messageSource.getMessage("merchants.errorUploadReceipt", null, locale));
        }
        String remark = invoiceConfirmData.getRemark();
        String prefix = "user: ";
        invoiceConfirmData.setRemark(remark, prefix);
        refillRequestDao.setStatusAndConfirmationDataById(requestId, newStatus, invoiceConfirmData);
    }


    @Transactional
    public void acceptRefillRequest(RefillRequestAcceptDto requestAcceptDto) {
        Integer requestId = requestAcceptDto.getRequestId();
        RefillRequestFlatDto refillRequestFlatDto = acceptRefill(requestAcceptDto);
        /**/
        if (refillRequestFlatDto.getStatus().isSuccessEndStatus()) {
            Locale locale = new Locale(userService.getPreferedLang(refillRequestFlatDto.getUserId()));
            String title = messageSource.getMessage("refill.accepted.title", new Integer[]{requestId}, locale);
            String comment = messageSource.getMessage("merchants.refillNotification.".concat(refillRequestFlatDto.getStatus().name()),
                    new Integer[]{requestId},
                    locale);
            String userEmail = userService.getEmailById(refillRequestFlatDto.getUserId());
            userService.addUserComment(REFILL_ACCEPTED, comment, userEmail, false);
            notificationService.notifyUser(refillRequestFlatDto.getUserId(), NotificationEvent.IN_OUT, title, comment);
        }
    }

    private RefillRequestFlatDto acceptRefill(RefillRequestAcceptDto requestAcceptDto) {
        ProfileData profileData = new ProfileData(1000);
        Integer requestId = requestAcceptDto.getRequestId();
        BigDecimal factAmount = requestAcceptDto.getAmount();
        Integer requesterAdminId = requestAcceptDto.getRequesterAdminId();
        String merchantTransactionId = requestAcceptDto.getMerchantTransactionId();
        try {
            RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                    .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
            RefillStatusEnum currentStatus = refillRequest.getStatus();
            if (currentStatus.isSuccessEndStatus()) {
                throw new RefillRequestAlreadyAcceptedException(refillRequest.toString());
            }
            Predicate<RefillRequestFlatDto> predicate = requestAcceptDto.getPredicate();
            if (predicate != null) {
                Boolean checkResult = predicate.test(refillRequest);
                if (!checkResult) {
                    throw new RefillRequestConditionsForAcceptAreCorruptedException(requestAcceptDto.toString());
                }
            }
            InvoiceActionTypeEnum action = requestAcceptDto.isToMainAccountTransferringConfirmNeeded() ? REQUEST_INNER_TRANSFER :
                    refillRequest.getStatus().availableForAction(ACCEPT_HOLDED) ? ACCEPT_HOLDED : ACCEPT_AUTO;
            RefillStatusEnum newStatus = requesterAdminId == null ?
                    (RefillStatusEnum) currentStatus.nextState(action) :
                    checkPermissionOnActionAndGetNewStatus(requesterAdminId, refillRequest, action);
            refillRequestDao.setStatusById(requestId, newStatus);
            refillRequestDao.setHolderById(requestId, requesterAdminId);
            if (!merchantTransactionId.equals(refillRequest.getMerchantTransactionId())) {
                try {
                    refillRequestDao.setMerchantTransactionIdById(requestId, merchantTransactionId);
                } catch (DuplicatedMerchantTransactionIdOrAttemptToRewriteException e) {
                    throw new RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException(merchantTransactionId);
                }
            }
            refillRequest.setStatus(newStatus);
            refillRequest.setAdminHolderId(requesterAdminId);
            refillRequest.setMerchantTransactionId(merchantTransactionId);
            String remark = requestAcceptDto.getRemark();
            String prefix = "admin (#"
                    .concat(refillRequest.getAdminHolderId() == null ? "null" : refillRequest.getAdminHolderId().toString())
                    .concat("): ");
            refillRequest.setRemark(remark, prefix);
            remark = refillRequest.getRemark();
            if (!StringUtils.isEmpty(remark)) {
                refillRequestDao.setRemarkById(requestId, remark);
            }
            profileData.setTime1();
            /**/
            Integer userWalletId = walletService.getWalletId(refillRequest.getUserId(), refillRequest.getCurrencyId());
            /**/
            BigDecimal commission = commissionService.calculateCommissionForRefillAmount(factAmount, refillRequest.getCommissionId());
            BigDecimal amountToEnroll = BigDecimalProcessing.doAction(factAmount, commission, SUBTRACT);
            /**/
            WalletOperationData walletOperationData = new WalletOperationData();
            walletOperationData.setOperationType(INPUT);
            walletOperationData.setWalletId(userWalletId);
            walletOperationData.setAmount(amountToEnroll);
            walletOperationData.setBalanceType(ACTIVE);
            walletOperationData.setCommission(new Commission(refillRequest.getCommissionId()));
            walletOperationData.setCommissionAmount(commission);
            walletOperationData.setSourceType(TransactionSourceType.REFILL);
            walletOperationData.setSourceId(refillRequest.getId());
            String description = transactionDescription.get(currentStatus, action);
            walletOperationData.setDescription(description);
            WalletTransferStatus walletTransferStatus = walletService.walletBalanceChange(walletOperationData);
            if (walletTransferStatus != SUCCESS) {
                throw new RefillRequestRevokeException(walletTransferStatus.name());
            }
            profileData.setTime2();
            CompanyWallet companyWallet = companyWalletService.findByCurrency(new Currency(refillRequest.getCurrencyId()));
            companyWalletService.deposit(
                    companyWallet,
                    amountToEnroll,
                    walletOperationData.getCommissionAmount()
            );
            profileData.setTime3();
            return refillRequest;
        } finally {
            profileData.checkAndLog("slow accept RefillRequest: " + requestId + " profile: " + profileData);
        }
    }

    @Transactional(readOnly = true)
    public RefillRequestFlatDto getFlatById(Integer id) {
        return refillRequestDao.getFlatById(id)
                .orElseThrow(() -> new RefillRequestNotFoundException(id.toString()));
    }

    @Transactional
    public void revokeRefillRequest(int requestId) {
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        RefillStatusEnum currentStatus = refillRequest.getStatus();
        InvoiceActionTypeEnum action = REVOKE;
        RefillStatusEnum newStatus = (RefillStatusEnum) currentStatus.nextState(action);
        refillRequestDao.setStatusById(requestId, newStatus);
    }

    @Transactional(readOnly = true)
    public List<InvoiceBank> findBanksForCurrency(Integer currencyId) {
        return refillRequestDao.findInvoiceBankListByCurrency(currencyId);
    }

    @Transactional
    public Map<String, String> correctAmountAndCalculateCommission(
            Integer userId,
            BigDecimal amount,
            Integer currencyId,
            Integer merchantId,
            Locale locale) {
        OperationType operationType = INPUT;
        BigDecimal addition = currencyService.computeRandomizedAddition(currencyId, operationType);
        amount = amount.add(addition);
        merchantService.checkAmountForMinSum(merchantId, currencyId, amount);
        Merchant merchant = merchantService.findById(merchantId);
        Map<String, String> result = commissionService.computeCommissionAndMapAllToString(
                userId,
                amount,
                operationType,
                currencyId,
                merchantId,
                locale,
                null);
        result.put("addition", addition.toString());
        return result;
    }

    @Transactional
    public DataTable<List<RefillRequestsAdminTableDto>> getRefillRequestByStatusList(
            List<Integer> requestStatus,
            DataTableParams dataTableParams,
            RefillFilterData refillFilterData,
            String authorizedUserEmail,
            Locale locale) {
        Integer authorizedUserId = userService.getIdByEmail(authorizedUserEmail);
        PagingData<List<RefillRequestFlatDto>> result = refillRequestDao.getPermittedFlatByStatus(
                requestStatus,
                authorizedUserId,
                dataTableParams,
                refillFilterData);
        DataTable<List<RefillRequestsAdminTableDto>> output = new DataTable<>();
        output.setData(result.getData().stream()
                .map(e -> new RefillRequestsAdminTableDto(e, refillRequestDao.getAdditionalDataForId(e.getId())))
                .peek(e -> e.setButtons(
                        inputOutputService.generateAndGetButtonsSet(
                                e.getStatus(),
                                e.getInvoiceOperationPermission(),
                                authorizedUserId.equals(e.getAdminHolderId()),
                                locale)
                ))
                .collect(Collectors.toList())
        );
        output.setRecordsTotal(result.getTotal());
        output.setRecordsFiltered(result.getFiltered());
        return output;
    }

    @Transactional(readOnly = true)
    public boolean checkInputRequestsLimit(int currencyId, String email) {
        return refillRequestDao.checkInputRequests(currencyId, email);
    }

    @Transactional
    public void takeInWorkRefillRequest(int requestId, Integer requesterAdminId) {
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        InvoiceActionTypeEnum action = TAKE_TO_WORK;
        RefillStatusEnum newStatus = checkPermissionOnActionAndGetNewStatus(requesterAdminId, refillRequest, action);
        refillRequestDao.setStatusById(requestId, newStatus);
        /**/
        refillRequestDao.setHolderById(requestId, requesterAdminId);
    }

    @Transactional
    public void returnFromWorkRefillRequest(int requestId, Integer requesterAdminId) {
        RefillRequestFlatDto withdrawRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        InvoiceActionTypeEnum action = RETURN_FROM_WORK;
        RefillStatusEnum newStatus = checkPermissionOnActionAndGetNewStatus(requesterAdminId, withdrawRequest, action);
        refillRequestDao.setStatusById(requestId, newStatus);
        /**/
        refillRequestDao.setHolderById(requestId, null);
    }

    @Transactional
    public void declineRefillRequest(int requestId, Integer requesterAdminId, String comment) {
        ProfileData profileData = new ProfileData(1000);
        try {
            RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                    .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
            RefillStatusEnum currentStatus = refillRequest.getStatus();
            InvoiceActionTypeEnum action = refillRequest.getStatus().availableForAction(DECLINE_HOLDED) ? DECLINE_HOLDED : DECLINE;
            RefillStatusEnum newStatus = checkPermissionOnActionAndGetNewStatus(requesterAdminId, refillRequest, action);
            refillRequestDao.setStatusById(requestId, newStatus);
            refillRequestDao.setHolderById(requestId, requesterAdminId);
            refillRequest.setStatus(newStatus);
            refillRequest.setAdminHolderId(requesterAdminId);
            profileData.setTime1();
            /**/
            Locale locale = new Locale(userService.getPreferedLang(refillRequest.getUserId()));
            String title = messageSource.getMessage("refill.declined.title", new Integer[]{requestId}, locale);
            if (StringUtils.isEmpty(comment)) {
                comment = messageSource.getMessage("merchants.refillNotification.".concat(newStatus.name()), new Integer[]{requestId}, locale);
            }
            String prefix = "admin (#"
                    .concat(refillRequest.getAdminHolderId() == null ? "null" : refillRequest.getAdminHolderId().toString())
                    .concat("): ");
            refillRequest.setRemark(comment, prefix);
            String remark = refillRequest.getRemark();
            if (!StringUtils.isEmpty(remark)) {
                refillRequestDao.setRemarkById(requestId, remark);
            }
            String userEmail = userService.getEmailById(refillRequest.getUserId());
            userService.addUserComment(REFILL_DECLINE, comment, userEmail, false);
            notificationService.notifyUser(refillRequest.getUserId(), NotificationEvent.IN_OUT, title, comment);
            profileData.setTime3();
        } finally {
            profileData.checkAndLog("slow decline RefillRequest: " + requestId + " profile: " + profileData);
        }
    }

    @Override
    @Transactional
    public void autoAcceptRefillRequest(RefillRequestAcceptDto requestAcceptDto) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = requestAcceptDto.getRequestId();
        if (requestId == null) {
            Optional<Integer> requestIdOptional = getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(
                    requestAcceptDto.getAddress(),
                    requestAcceptDto.getMerchantId(),
                    requestAcceptDto.getCurrencyId());
            if (requestIdOptional.isPresent()) {
                requestId = requestIdOptional.get();
            }
        }
        if (requestId != null) {
            requestAcceptDto.setRequestId(requestId);
            RefillRequestFlatDto refillRequestFlatDto = acceptRefill(requestAcceptDto);
            /**/
            Locale locale = new Locale(userService.getPreferedLang(refillRequestFlatDto.getUserId()));
            String title = messageSource.getMessage("refill.accepted.title", new Integer[]{requestId}, locale);
            String comment = messageSource.getMessage("merchants.refillNotification.".concat(refillRequestFlatDto.getStatus().name()),
                    new Integer[]{requestId},
                    locale);
            String userEmail = userService.getEmailById(refillRequestFlatDto.getUserId());
            userService.addUserComment(REFILL_ACCEPTED, comment, userEmail, false);
            notificationService.notifyUser(refillRequestFlatDto.getUserId(), NotificationEvent.IN_OUT, title, comment);
        } else {
            throw new RefillRequestAppropriateNotFoundException(requestAcceptDto.toString());
        }
    }

    @Override
    @Transactional
    public Integer createRefillRequestByFact(RefillRequestAcceptDto requestAcceptDto) {
        log.debug("Creating request by fact: " + requestAcceptDto);
        String address = requestAcceptDto.getAddress();
        Integer currencyId = requestAcceptDto.getCurrencyId();
        Integer merchantId = requestAcceptDto.getMerchantId();
        BigDecimal amount = requestAcceptDto.getAmount();
        Integer userId = getUserIdByAddressAndMerchantIdAndCurrencyId(address, merchantId, currencyId)
                .orElseThrow(() -> new CreatorForTheRefillRequestNotDefinedException(String.format("address: %s currency: %s merchant: %s amount: %s",
                        address, currencyId, merchantId, amount)));
        Locale locale = new Locale(userService.getPreferedLang(userId));
        Integer commissionId = commissionService.findCommissionByTypeAndRole(INPUT, userService.getUserRoleFromDB(userId)).getId();
        RefillStatusEnum beginStatus = (RefillStatusEnum) RefillStatusEnum.X_STATE.nextState(CREATE_BY_FACT);
        RefillRequestCreateDto request = new RefillRequestCreateDto();
        request.setUserId(userId);
        request.setStatus(beginStatus);
        request.setCurrencyId(currencyId);
        request.setMerchantId(merchantId);
        request.setAmount(amount);
        request.setCommissionId(commissionId);
        request.setAddress(address);
        request.setNeedToCreateRefillRequestRecord(true);
        Integer requestId = createRefillByFact(request).orElseThrow(() -> new RefillRequestCreationByFactException(requestAcceptDto.toString()));
        request.setId(requestId);
        try {
            sendRefillNotificationAfterCreationByFact(
                    request,
                    locale);
        } catch (MailException e) {
            log.error(e);
        }
        return requestId;
    }

    public Optional<Integer> getRequestIdInPendingByAddressAndMerchantIdAndCurrencyId(
            String address,
            Integer merchantId,
            Integer currencyId) {
        List<InvoiceStatus> statusList = RefillStatusEnum.getAvailableForActionStatusesList(START_BCH_EXAMINE);
        return refillRequestDao.findIdWithoutConfirmationsByAddressAndMerchantIdAndCurrencyIdAndStatusId(
                address,
                merchantId,
                currencyId,
                statusList.stream().map(InvoiceStatus::getCode).collect(Collectors.toList()));
    }

    @Transactional
    public RefillRequestsAdminTableDto getRefillRequestById(
            Integer id,
            String authorizedUserEmail) {
        Integer authorizedUserId = userService.getIdByEmail(authorizedUserEmail);
        Integer userId = refillRequestDao.findUserIdById(id).orElse(null);
        authorizedUserId = authorizedUserId.equals(userId) ? null : authorizedUserId;
        RefillRequestFlatDto withdraw = refillRequestDao.getPermittedFlatById(
                id,
                authorizedUserId);
        DataTable<List<WithdrawRequestsAdminTableDto>> output = new DataTable<>();
        return new RefillRequestsAdminTableDto(withdraw, refillRequestDao.getAdditionalDataForId(withdraw.getId()));
    }

    private Optional<Integer> createRefill(RefillRequestCreateDto request) {
        if (request.getNeedToCreateRefillRequestRecord()) {
            RefillStatusEnum currentStatus = request.getStatus();
            Merchant merchant = merchantDao.findById(request.getMerchantId());
            InvoiceActionTypeEnum action = currentStatus.getStartAction(merchant);
            RefillStatusEnum newStatus = (RefillStatusEnum) currentStatus.nextState(action);
            request.setStatus(newStatus);
        }
        return refillRequestDao.create(request);
    }

    @Transactional
    public Integer manualCreateRefillRequestCrypto(RefillRequestManualDto refillDto, Locale locale) throws DuplicatedMerchantTransactionIdOrAttemptToRewriteException {
        User user = Preconditions.checkNotNull(userService.findByEmail(refillDto.getEmail()), "user not found");
        if (!this.checkInputRequestsLimit(refillDto.getCurrency(), refillDto.getEmail())) {
            throw new RequestLimitExceededException(messageSource.getMessage("merchants.InputRequestsLimit", null, locale));
        }
        Integer merchantId = Preconditions.checkNotNull(this.getMerchantIdByAddressAndCurrencyAndUser(
                refillDto.getAddress(),
                refillDto.getCurrency(),
                user.getId()), "address not found");
        Payment payment = new Payment(INPUT);
        payment.setCurrency(refillDto.getCurrency());
        payment.setMerchant(merchantId);
        payment.setSum(refillDto.getAmount() == null ? 0 : refillDto.getAmount().doubleValue());
        refillDto.setMerchantId(merchantId);
        CreditsOperation creditsOperation = inputOutputService.prepareCreditsOperation(payment, refillDto.getEmail(), locale)
                .orElseThrow(InvalidAmountException::new);
        RefillRequestCreateDto request = new RefillRequestCreateDto(
                new RefillRequestParamsDto(refillDto),
                creditsOperation,
                RefillStatusEnum.ON_PENDING,
                locale);
        request.setNeedToCreateRefillRequestRecord(true);
        Integer reqId = this.createRefillByFact(request).orElseThrow(() -> new RuntimeException("refiil not created"));
        ;
        if (!StringUtils.isEmpty(refillDto.getTxHash())) {
            refillRequestDao.setMerchantTransactionIdById(reqId, refillDto.getTxHash());
        }
        return reqId;
    }

    private Optional<Integer> createRefillByFact(RefillRequestCreateDto request) {
        return refillRequestDao.create(request);
    }

    private String sendRefillNotificationAfterCreation(
            RefillRequestCreateDto request,
            String addMessage,
            Locale locale) {
        String title = messageSource.getMessage("merchants.refillNotification.header", null, locale);
        Integer lifetime = merchantService.getMerchantCurrencyLifetimeByMerchantIdAndCurrencyId(request.getMerchantId(), request.getCurrencyId()).getRefillLifetimeHours();
        String mainNotificationMessageCodeSuffix = lifetime == 0 ? "" : ".lifetime";
        String mainNotificationMessageCode = "merchants.refillNotification.".concat(request.getStatus().name()).concat(mainNotificationMessageCodeSuffix);
        Object[] messageParams = {
                request.getCurrencyName(),
                request.getMerchantDescription(),
                lifetime
        };
        String mainNotification = messageSource.getMessage(mainNotificationMessageCode, messageParams, locale);
        String fullNotification = StringUtils.isEmpty(addMessage) ? mainNotification : mainNotification.concat("<br>").concat("<br>").concat(addMessage);
        notificationService.notifyUser(request.getUserId(), NotificationEvent.IN_OUT, title, fullNotification);
        return fullNotification;
    }

    private RefillStatusEnum checkPermissionOnActionAndGetNewStatus(Integer requesterAdminId, RefillRequestFlatDto refillRequest, InvoiceActionTypeEnum action) {
        Boolean requesterAdminIsHolder = requesterAdminId.equals(refillRequest.getAdminHolderId());
        InvoiceOperationPermission permission = userService.getCurrencyPermissionsByUserIdAndCurrencyIdAndDirection(
                requesterAdminId,
                refillRequest.getCurrencyId(),
                REFILL
        );
        InvoiceActionTypeEnum.InvoiceActionParamsValue paramsValue = InvoiceActionTypeEnum.InvoiceActionParamsValue.builder()
                .authorisedUserIsHolder(requesterAdminIsHolder)
                .permittedOperation(permission)
                .build();
        return (RefillStatusEnum) refillRequest.getStatus().nextState(action, paramsValue);
    }

    public Optional<InvoiceBank> findInvoiceBankById(Integer id) {
        return refillRequestDao.findInvoiceBankById(id);
    }

    public String getPaymentMessageForTag(String serviceBeanName, String tag, Locale locale) {
        IMerchantService merchantService = merchantServiceContext.getMerchantService(serviceBeanName);
        return merchantService.getPaymentMessage(tag, locale);
    }

    public Optional<Integer> getRequestIdReadyForAutoAcceptByAddressAndMerchantIdAndCurrencyId(String address, Integer merchantId, Integer currencyId) {
        List<InvoiceStatus> statusList = RefillStatusEnum.getAvailableForActionStatusesList(ACCEPT_AUTO);
        return refillRequestDao.findIdByAddressAndMerchantIdAndCurrencyIdAndStatusId(
                address,
                merchantId,
                currencyId,
                statusList.stream().map(InvoiceStatus::getCode).collect(Collectors.toList()));
    }

    @Override
    public Optional<Integer> getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address,
            Integer merchantId,
            Integer currencyId,
            String hash) {
        return refillRequestDao.findIdByAddressAndMerchantIdAndCurrencyIdAndHash(
                address,
                merchantId,
                currencyId,
                hash);
    }

    @Override
    public Optional<Integer> getRequestIdByMerchantIdAndCurrencyIdAndHash(int merchantId, int currencyId, String hash) {
        return refillRequestDao.findIdByMerchantIdAndCurrencyIdAndHash(
                merchantId,
                currencyId,
                hash);
    }

    @Override
    public List<RefillRequestAddressDto> findAddressDtos(Integer merchantId, Integer currencyId) {
        return refillRequestDao.findAddressDtosByMerchantAndCurrency(merchantId, currencyId);
    }

    @Override
    public void updateAddressNeedTransfer(String address, int merchantId, int currencyId, boolean isNeeded) {
        refillRequestDao.updateAddressNeedTransfer(address, merchantId, currencyId, isNeeded);
    }

    @Override
    public List<RefillRequestFlatDto> getInExamineWithChildTokensByMerchantIdAndCurrencyIdList(int merchantId, int currencyId) {
        List<InvoiceStatus> statusList = RefillStatusEnum.getAvailableForActionStatusesList(ACCEPT_AUTO);
        return refillRequestDao.findAllWithChildTokensWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(
                merchantId,
                currencyId,
                statusList.stream().map(InvoiceStatus::getCode).collect(Collectors.toList()));
    }

    @Override
    public Optional<Integer> getRequestIdByMerchantIdAndCurrencyIdAndHash(Integer merchantId, Integer currencyId, String hash) {
        return refillRequestDao.findIdByMerchantIdAndCurrencyIdAndHash(
                merchantId,
                currencyId,
                hash);
    }


    @Override
    public List<RefillRequestAddressDto> findAllAddressesNeededToTransfer(int merchantId, int currencyId) {
        return null;
    }

    @Override
    public List<RefillRequestFlatDto> getInExamineByMerchantIdAndCurrencyIdList(Integer merchantId, Integer currencyId) {
        List<InvoiceStatus> statusList = RefillStatusEnum.getAvailableForActionStatusesList(ACCEPT_AUTO);
        return refillRequestDao.findAllWithConfirmationsByMerchantIdAndCurrencyIdAndStatusId(
                merchantId,
                currencyId,
                statusList.stream().map(InvoiceStatus::getCode).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void putOnBchExamRefillRequest(RefillRequestPutOnBchExamDto onBchExamDto) throws RefillRequestAppropriateNotFoundException {
        log.debug("Put on bch exam: " + onBchExamDto);
        Integer requestId = onBchExamDto.getRequestId();
        if (requestId == null) {
            Optional<Integer> requestIdOptional = getRequestIdInPendingByAddressAndMerchantIdAndCurrencyId(
                    onBchExamDto.getAddress(),
                    onBchExamDto.getMerchantId(),
                    onBchExamDto.getCurrencyId());
            if (requestIdOptional.isPresent()) {
                requestId = requestIdOptional.get();
            }
        }
        if (requestId != null) {
            onBchExamDto.setRequestId(requestId);
            RefillRequestFlatDto refillRequestFlatDto = putOnBchExam(onBchExamDto);
            /**/
            Locale locale = new Locale(userService.getPreferedLang(refillRequestFlatDto.getUserId()));
            String title = messageSource.getMessage("refill.accepted.title", new Integer[]{requestId}, locale);
            String comment = messageSource.getMessage("merchants.refillNotification.".concat(refillRequestFlatDto.getStatus().name()),
                    new Integer[]{requestId},
                    locale);
            String userEmail = userService.getEmailById(refillRequestFlatDto.getUserId());
            userService.addUserComment(REFILL_ACCEPTED, comment, userEmail, false);
            notificationService.notifyUser(refillRequestFlatDto.getUserId(), NotificationEvent.IN_OUT, title, comment);
        } else {
            throw new RefillRequestAppropriateNotFoundException(onBchExamDto.toString());
        }
    }

    @Override
    public List<String> findAllAddresses(Integer merchantId, Integer currencyId){
        return refillRequestDao.findAllAddresses(merchantId, currencyId);
    }

    @Override
    @Transactional
    public void setConfirmationCollectedNumber(RefillRequestSetConfirmationsNumberDto confirmationsNumberDto) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = confirmationsNumberDto.getRequestId();
        if (requestId == null) {
            Optional<Integer> requestIdOptional = getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(
                    confirmationsNumberDto.getAddress(),
                    confirmationsNumberDto.getMerchantId(),
                    confirmationsNumberDto.getCurrencyId(),
                    confirmationsNumberDto.getHash());
            if (requestIdOptional.isPresent()) {
                requestId = requestIdOptional.get();
            }
        }
        if (requestId != null) {
            String hash = confirmationsNumberDto.getHash();
            BigDecimal amount = confirmationsNumberDto.getAmount();
            Integer confirmations = confirmationsNumberDto.getConfirmations();
            String blockhash = confirmationsNumberDto.getBlockhash();
            RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                    .orElseThrow(() -> new RefillRequestNotFoundException(confirmationsNumberDto.toString()));
            RefillStatusEnum currentStatus = refillRequest.getStatus();
            if (!currentStatus.availableForAction(ACCEPT_AUTO)) {
                throw new RefillRequestIllegalStatusException(refillRequest.toString());
            }
            if (!hash.equals(refillRequest.getMerchantTransactionId())) {
                try {
                    refillRequestDao.setMerchantTransactionIdById(requestId, hash);
                } catch (DuplicatedMerchantTransactionIdOrAttemptToRewriteException e) {
                    throw new RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException(hash);
                }
            }
            refillRequestDao.setConfirmationsNumberByRequestId(requestId, amount, confirmations, blockhash);
        } else {
            throw new RefillRequestAppropriateNotFoundException(confirmationsNumberDto.toString());
        }
    }

    @Override
    public Optional<RefillRequestFlatDto> findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(
            String address, Integer merchantId,
            Integer currencyId,
            String hash) {
        return refillRequestDao.findFlatByAddressAndMerchantIdAndCurrencyIdAndHash(address, merchantId, currencyId, hash);
    }

    private RefillRequestFlatDto putOnBchExam(RefillRequestPutOnBchExamDto onBchExamDto) {
        Integer requestId = onBchExamDto.getRequestId();
        String hash = onBchExamDto.getHash();
        BigDecimal amount = onBchExamDto.getAmount();
        String blockhash = onBchExamDto.getBlockhash();
        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        RefillStatusEnum currentStatus = refillRequest.getStatus();
        InvoiceActionTypeEnum action = START_BCH_EXAMINE;
        RefillStatusEnum newStatus = (RefillStatusEnum) currentStatus.nextState(action);
        refillRequestDao.setStatusById(requestId, newStatus);
        try {
            refillRequestDao.setMerchantTransactionIdById(requestId, hash);
        } catch (DuplicatedMerchantTransactionIdOrAttemptToRewriteException e) {
            throw new RefillRequestDuplicatedMerchantTransactionIdOrAttemptToRewriteException(onBchExamDto.toString());
        }
        refillRequest.setStatus(newStatus);
        refillRequest.setMerchantTransactionId(hash);
        refillRequestDao.setConfirmationsNumberByRequestId(requestId, amount, 0, blockhash);
        return refillRequest;
    }

    @Override
    @Transactional
    public Optional<Integer> getUserIdByAddressAndMerchantIdAndCurrencyId(
            String address,
            Integer merchantId,
            Integer currencyId) {
        return refillRequestDao.findUserIdByAddressAndMerchantIdAndCurrencyId(address, merchantId, currencyId);
    }

    private String sendRefillNotificationAfterCreationByFact(
            RefillRequestCreateDto request,
            Locale locale) {
        String title = messageSource.getMessage("merchants.refillNotification.header", null, locale);
        String mainNotificationMessageCodeSuffix = "";
        String mainNotificationMessageCode = "merchants.refillNotification.".concat(request.getStatus().name()).concat(mainNotificationMessageCodeSuffix);
        Object[] messageParams = {
                request.getId(),
                request.getMerchantDescription()
        };
        String notification = messageSource.getMessage(mainNotificationMessageCode, messageParams, locale);
        notificationService.notifyUser(request.getUserId(), NotificationEvent.IN_OUT, title, notification);
        return notification;
    }

    @Override
    public int getTxOffsetForAddress(String address) {
        return refillRequestDao.getTxOffsetForAddress(address);
    }

    @Override
    public void updateTxOffsetForAddress(String address, Integer offset) {
        refillRequestDao.updateTxOffsetForAddress(address, offset);
    }

    @Override
    public boolean existsClosedRefillRequestForAddress(String s, int id, int id1) {
        return false;
    }

    @Override
    public List<RefillRequestAddressDto> findByAddressMerchantAndCurrency(String address, int merchantId, int currencyId) {
        return refillRequestDao.findByAddressMerchantAndCurrency(address, merchantId, currencyId);
    }

    @Override
    public Optional<String> getLastBlockHashForMerchantAndCurrency(Integer merchantId, Integer currencyId) {
        return refillRequestDao.getLastBlockHashForMerchantAndCurrency(merchantId, currencyId);
    }

}

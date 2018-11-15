package com.exrates.inout.service.impl;

import com.exrates.inout.dao.InputOutputDao;
import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.dto.MyInputOutputHistoryDto;
import com.exrates.inout.domain.enums.MerchantProcessType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.invoice.*;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.other.PaginationWrapper;
import com.exrates.inout.exceptions.UnsupportedMerchantException;
import com.exrates.inout.exceptions.UserNotFoundException;
import com.exrates.inout.service.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.exrates.inout.domain.enums.OperationType.INPUT;
import static com.exrates.inout.domain.enums.invoice.RefillStatusEnum.ON_BCH_EXAM;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.EMPTY_LIST;


@Service
public class InputOutputServiceImpl implements InputOutputService {

    private static final Logger log = LogManager.getLogger("inputoutput");

    @Autowired
    private MessageSource messageSource;

    @Autowired
    InputOutputDao inputOutputDao;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    MerchantServiceContext merchantServiceContext;


    private void setAdditionalFields(List<MyInputOutputHistoryDto> inputOutputList, Locale locale) {
        inputOutputList.forEach(e ->
        {
            e.setSummaryStatus(generateAndGetSummaryStatus(e, locale));
            e.setButtons(generateAndGetButtonsSet(e.getStatus(), null, false, locale));
            e.setAuthorisedUserId(e.getUserId());
        });
    }

    @Override
    public PaginationWrapper<List<MyInputOutputHistoryDto>> findUnconfirmedInvoices(String userEmail, String currencyName, Integer limit, Integer offset, Locale locale) {
        PaginationWrapper<List<MyInputOutputHistoryDto>> result = inputOutputDao.findUnconfirmedInvoices(userService.getIdByEmail(userEmail),
                currencyService.findByName(currencyName).getId(), limit, offset);
        setAdditionalFields(result.getData(), locale);
        return result;
    }


    @Override
    public List<Map<String, Object>> generateAndGetButtonsSet(
            InvoiceStatus status,
            InvoiceOperationPermission permittedOperation,
            boolean authorisedUserIsHolder,
            Locale locale) {
        if (status == null) return EMPTY_LIST;
        InvoiceActionTypeEnum.InvoiceActionParamsValue paramsValue = InvoiceActionTypeEnum.InvoiceActionParamsValue.builder()
                .authorisedUserIsHolder(authorisedUserIsHolder)
                .permittedOperation(permittedOperation)
                .build();
        return status.getAvailableActionList(paramsValue).stream()
                .filter(e -> e.getActionTypeButton() != null)
                .map(e -> new HashMap<String, Object>(e.getActionTypeButton().getProperty()))
                .peek(e -> e.put("buttonTitle", messageSource.getMessage((String) e.get("buttonTitle"), null, locale)))
                .collect(Collectors.toList());
    }


    private String generateAndGetSummaryStatus(MyInputOutputHistoryDto row, Locale locale) {
        log.debug("status1 {}", row);
        switch (row.getSourceType()) {
            case REFILL: {
                RefillStatusEnum status = (RefillStatusEnum) row.getStatus();
                if (status == ON_BCH_EXAM) {
                    IRefillable merchant = (IRefillable) merchantServiceContext
                            .getMerchantService(merchantService.findByName(row.getMerchantName()).getServiceBeanName());
                    String message;
                    Integer confirmationsCount = merchant.minConfirmationsRefill();
                    if (confirmationsCount == null) {
                        message = messageSource.getMessage("merchants.refill.TAKEN_FROM_EXAM", null, locale);
                    } else {
                        String confirmations = row.getConfirmation() == null ? "0" : row.getConfirmation().toString();
                        message = confirmations.concat("/").concat(confirmationsCount.toString());
                    }
                    return message;
                } else {
                    return messageSource.getMessage("merchants.refill.".concat(status.name()), null, locale);
                }
            }
            case WITHDRAW: {
                WithdrawStatusEnum status = (WithdrawStatusEnum) row.getStatus();
                return messageSource.getMessage("merchants.withdraw.".concat(status.name()), null, locale);
            }
            case USER_TRANSFER: {
                TransferStatusEnum status = (TransferStatusEnum) row.getStatus();
                return messageSource.getMessage("merchants.transfer.".concat(status.name()), null, locale);
            }
            default: {
                return row.getTransactionProvided();
            }
        }
    }

    @Override
    @Transactional
    public Optional<CreditsOperation> prepareCreditsOperation(Payment payment, String userEmail, Locale locale) {
        merchantService.checkMerchantIsBlocked(payment.getMerchant(), payment.getCurrency(), payment.getOperationType());
        OperationType operationType = payment.getOperationType();
        BigDecimal amount = valueOf(payment.getSum());
        Merchant merchant = merchantService.findById(payment.getMerchant());
        Currency currency = currencyService.findById(payment.getCurrency());
        String destination = payment.getDestination();
        String destinationTag = payment.getDestinationTag();
        if (!(merchant.getProcessType() == MerchantProcessType.CRYPTO && operationType == OperationType.INPUT)) {
            try {
                merchantService.checkAmountForMinSum(merchant.getId(), currency.getId(), amount);
            } catch (EmptyResultDataAccessException e) {
                final String exceptionMessage = "MerchantService".concat(operationType == INPUT ?
                        "Input" : "Output");
                throw new UnsupportedMerchantException(exceptionMessage);
            }
        }
        User user = userService.findByEmail(userEmail);
        Wallet wallet = walletService.findByUserAndCurrency(user, currency);
        CommissionDataDto commissionData = commissionService.normalizeAmountAndCalculateCommission(
                user.getId(),
                amount,
                operationType,
                currency.getId(),
                merchant.getId(), payment.getDestinationTag());
        TransactionSourceType transactionSourceType = operationType.getTransactionSourceType();
        User recipient = null;
        try {
            if (!StringUtils.isEmpty(payment.getRecipient())) {
                recipient = userService.getIdByNickname(payment.getRecipient()) > 0 ?
                        userService.findByNickname(payment.getRecipient()) : userService.findByEmail(payment.getRecipient());
            }
        } catch (RuntimeException e) {
            throw new UserNotFoundException(messageSource.getMessage("transfer.nonExistentUser", new Object[]{payment.getRecipient()}, locale));
        }

        Wallet recipientWallet = recipient == null ? null : walletService.findByUserAndCurrency(recipient, currency);
        CreditsOperation creditsOperation = new CreditsOperation.Builder()
                .initialAmount(commissionData.getAmount())
                .amount(commissionData.getResultAmount())
                .commissionAmount(commissionData.getCompanyCommissionAmount())
                .commission(commissionData.getCompanyCommission())
                .operationType(operationType)
                .user(user)
                .currency(currency)
                .wallet(wallet)
                .merchant(merchant)
                .merchantCommissionAmount(commissionData.getMerchantCommissionAmount())
                .destination(destination)
                .destinationTag(destinationTag)
                .transactionSourceType(transactionSourceType)
                .recipient(recipient)
                .recipientWallet(recipientWallet)
                .build();
        return Optional.of(creditsOperation);
    }

}

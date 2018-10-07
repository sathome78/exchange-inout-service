package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.exrates.inout.util.BigDecimalProcessing;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.exrates.inout.domain.enums.TransactionSourceType.WITHDRAW;

@EqualsAndHashCode(callSuper = true)
@Data
public class WithdrawRequestsAdminTableDto extends OnlineTableDto {
    private Integer id;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateCreation;
    private Integer userId;
    private String userEmail;
    private BigDecimal amount;
    private String currencyName;
    private BigDecimal commissionAmount;
    private BigDecimal netAmount;
    private BigDecimal netAmountCorrectedForMerchantCommission;
    private String merchantName;
    private String wallet;
    private String txHash;
    private String destinationTag;
    private Integer adminHolderId;
    private String adminHolderEmail;
    private String recipientBankName;
    private String recipientBankCode;
    private String userFullName;
    private String remark;
    private WithdrawStatusEnum status;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime statusModificationDate;
    private TransactionSourceType sourceType = WITHDRAW;
    private InvoiceOperationPermission invoiceOperationPermission;
    private Boolean isEndStatus;
    private Boolean authorizedUserIsHolder;
    private List<Map<String, Object>> buttons;

    public WithdrawRequestsAdminTableDto(
            WithdrawRequestFlatDto withdrawRequestFlatDto,
            WithdrawRequestFlatAdditionalDataDto withdrawRequestFlatAdditionalDataDto) {
        this.id = withdrawRequestFlatDto.getId();
        this.dateCreation = withdrawRequestFlatDto.getDateCreation();
        this.userId = withdrawRequestFlatDto.getUserId();
        this.userEmail = withdrawRequestFlatAdditionalDataDto.getUserEmail();
        this.amount = withdrawRequestFlatDto.getAmount();
        this.currencyName = withdrawRequestFlatAdditionalDataDto.getCurrencyName();
        this.commissionAmount = withdrawRequestFlatDto.getCommissionAmount();
        this.netAmount = BigDecimalProcessing.doAction(this.amount, this.commissionAmount, ActionType.SUBTRACT);
        if (withdrawRequestFlatDto.getMerchantCommissionAmount() != null) {
            this.netAmountCorrectedForMerchantCommission = withdrawRequestFlatAdditionalDataDto.getIsMerchantCommissionSubtractedForWithdraw() ?
                    BigDecimalProcessing.doAction(this.netAmount, withdrawRequestFlatDto.getMerchantCommissionAmount(), ActionType.SUBTRACT) :
                    this.netAmount;
        }
        this.merchantName = withdrawRequestFlatAdditionalDataDto.getMerchantName();
        this.wallet = withdrawRequestFlatDto.getWallet();
        this.txHash = withdrawRequestFlatDto.getTransactionHash();
        this.destinationTag = withdrawRequestFlatDto.getDestinationTag();
        this.adminHolderId = withdrawRequestFlatDto.getAdminHolderId();
        this.adminHolderEmail = withdrawRequestFlatAdditionalDataDto.getAdminHolderEmail();
        this.recipientBankName = withdrawRequestFlatDto.getRecipientBankName();
        this.recipientBankCode = withdrawRequestFlatDto.getRecipientBankCode();
        this.userFullName = withdrawRequestFlatDto.getUserFullName();
        this.remark = withdrawRequestFlatDto.getRemark();
        this.status = withdrawRequestFlatDto.getStatus();
        this.statusModificationDate = withdrawRequestFlatDto.getStatusModificationDate();
        this.invoiceOperationPermission = withdrawRequestFlatDto.getInvoiceOperationPermission();
        this.isEndStatus = this.status.isEndStatus();
        this.buttons = null;
    }
}

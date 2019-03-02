package com.exrates.inout.domain.main;

import com.exrates.inout.domain.dto.WithdrawRequestCreateDto;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class WithdrawRequest implements Serializable {
    private Integer id;
    private String wallet;
    private String destinationTag;
    private Integer userId;
    private String userEmail;
    private String recipientBankName;
    private String recipientBankCode;
    private String userFullName;
    private String remark;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private Integer commissionId;
    private WithdrawStatusEnum status;
    private LocalDateTime dateCreation;
    private LocalDateTime statusModificationDate;
    private Currency currency;
    private Merchant merchant;
    private Integer adminHolderId;

    public WithdrawRequest(WithdrawRequestCreateDto withdrawRequestCreateDto) {
        this.id = withdrawRequestCreateDto.getId();
        this.wallet = withdrawRequestCreateDto.getDestinationWallet();
        this.destinationTag = withdrawRequestCreateDto.getDestinationTag();
        this.userId = withdrawRequestCreateDto.getUserId();
        this.userEmail = withdrawRequestCreateDto.getUserEmail();
        this.recipientBankName = withdrawRequestCreateDto.getRecipientBankName();
        this.recipientBankCode = withdrawRequestCreateDto.getRecipientBankCode();
        this.userFullName = withdrawRequestCreateDto.getUserFullName();
        this.remark = withdrawRequestCreateDto.getRemark();
        this.amount = withdrawRequestCreateDto.getAmount();
        this.commissionAmount = withdrawRequestCreateDto.getCommission();
        this.status = WithdrawStatusEnum.convert(withdrawRequestCreateDto.getStatusId());
    }

}

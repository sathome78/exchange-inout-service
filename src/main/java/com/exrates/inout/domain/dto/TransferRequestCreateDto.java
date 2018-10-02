package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.invoice.TransferStatusEnum;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.util.BigDecimalProcessing;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Locale;

import static com.exrates.inout.domain.enums.ActionType.ADD;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransferRequestCreateDto {
    private Integer id;
    private Integer userId;
    private String userEmail;
    private Integer userWalletId;
    private Integer currencyId;
    private String currencyName;
    private BigDecimal amount;
    private BigDecimal commission;
    private Integer commissionId;
    private Integer merchantId;
    private String merchantDescription;
    private String serviceBeanName;
    private Integer statusId;
    private String recipient;
    private Integer recipientId;
    private Integer recipientWalletId;
    private Locale locale;
    private Boolean isVoucher;
    private String hash;

    public TransferRequestCreateDto(TransferRequestParamsDto paramsDto, CreditsOperation creditsOperation, TransferStatusEnum status, Locale locale) {
        this.currencyId = paramsDto.getCurrency();
        this.amount = paramsDto.getSum();
        this.merchantId = paramsDto.getMerchant();
        /**/
        this.userId = creditsOperation.getUser().getId();
        this.userEmail = creditsOperation.getUser().getEmail();
        this.userWalletId = creditsOperation.getWallet().getId();
        this.currencyName = creditsOperation.getCurrency().getName();
        this.commission = BigDecimalProcessing.doAction(creditsOperation.getMerchantCommissionAmount(), creditsOperation.getCommissionAmount(), ADD);
        ;
        this.commissionId = creditsOperation.getCommission().getId();
        this.recipient = creditsOperation.getRecipient() == null ? null : creditsOperation.getRecipient().getNickname();
        this.recipientId = creditsOperation.getRecipient() == null ? null : creditsOperation.getRecipient().getId();
        this.recipientWalletId = creditsOperation.getRecipientWallet() == null ? null : creditsOperation.getRecipientWallet().getId();
        this.serviceBeanName = creditsOperation.getMerchant().getServiceBeanName();
        this.merchantDescription = creditsOperation.getMerchant().getDescription();
        /**/
        this.statusId = status.getCode();
        /**/
        this.locale = locale;
    }

}

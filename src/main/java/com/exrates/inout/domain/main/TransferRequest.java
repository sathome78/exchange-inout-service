package com.exrates.inout.domain.main;

import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.domain.enums.invoice.TransferStatusEnum;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class TransferRequest {
    private Integer id;
    private Integer userId;
    private String userEmail;
    private BigDecimal amount;
    private BigDecimal commissionAmount;
    private Integer commissionId;
    private TransferStatusEnum status;
    private LocalDateTime dateCreation;
    private LocalDateTime statusModificationDate;
    private Currency currency;
    private Merchant merchant;
    private Integer recipientId;

    public TransferRequest(TransferRequestCreateDto transferRequestCreateDto) {
        this.id = transferRequestCreateDto.getId();
        this.userId = transferRequestCreateDto.getUserId();
        this.userEmail = transferRequestCreateDto.getUserEmail();
        this.amount = transferRequestCreateDto.getAmount();
        this.commissionAmount = transferRequestCreateDto.getCommission();
        this.status = TransferStatusEnum.convert(transferRequestCreateDto.getStatusId());
        this.recipientId = transferRequestCreateDto.getRecipientId();
    }

}

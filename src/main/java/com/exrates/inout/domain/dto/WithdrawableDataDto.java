package com.exrates.inout.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class WithdrawableDataDto {
    private Boolean additionalTagForWithdrawAddressIsUsed;
    private String additionalWithdrawFieldName;

}

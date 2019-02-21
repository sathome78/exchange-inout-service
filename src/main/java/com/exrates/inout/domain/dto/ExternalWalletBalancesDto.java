package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder(builderClassName = "Builder")
@AllArgsConstructor
@NoArgsConstructor
public class ExternalWalletBalancesDto {

    private Integer currencyId;
    private String currencyName;

    private BigDecimal usdRate;
    private BigDecimal btcRate;

    private BigDecimal mainBalance;
    private BigDecimal reservedBalance;

    private BigDecimal totalBalance;
    private BigDecimal totalBalanceUSD;
    private BigDecimal totalBalanceBTC;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime lastUpdatedDate;

    private boolean signOfCertainty;


    public static ExternalWalletBalancesDto getZeroBalances(Integer currencyId, String currencyName) {
        return ExternalWalletBalancesDto
                .builder()
                .currencyId(currencyId)
                .currencyName(currencyName)
                .usdRate(BigDecimal.ZERO)
                .btcRate(BigDecimal.ZERO)
                .mainBalance(BigDecimal.ZERO)
                .reservedBalance(BigDecimal.ZERO)
                .totalBalance(BigDecimal.ZERO)
                .totalBalanceUSD(BigDecimal.ZERO)
                .totalBalanceBTC(BigDecimal.ZERO)
                .build();

    }


}

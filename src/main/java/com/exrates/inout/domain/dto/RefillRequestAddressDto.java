package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class RefillRequestAddressDto {
    private Integer id;
    private Integer currencyId;
    private Integer merchantId;
    private String address;
    private Integer userId;
    private String privKey;
    private String pubKey;
    private String brainPrivKey;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime dateGeneration;
    private Integer confirmedTxOffset;
    private boolean needTransfer;

}

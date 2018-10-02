package com.exrates.inout.domain.main;

import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefillRequestAddressShortDto {

    private String userEmail;
    private String address;
    private String addressFieldName;
    private String currencyName;
    private int merchantId;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime generationDate;
}

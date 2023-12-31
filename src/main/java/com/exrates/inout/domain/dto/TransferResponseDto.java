package com.exrates.inout.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
public class TransferResponseDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String balance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String hash;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String recipient;
}

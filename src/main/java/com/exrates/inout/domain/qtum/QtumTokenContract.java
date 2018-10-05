package com.exrates.inout.domain.qtum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumTokenContract {
    private String address;
    private QtumTokenContractExecutionResult executionResult;
}

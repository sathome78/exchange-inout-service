package com.exrates.inout.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JsonRpcResponseError {
    private Integer code;
    private String message;
}

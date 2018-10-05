package com.exrates.inout.domain.qtum;

import com.exrates.inout.domain.JsonRpcResponseError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumJsonRpcResponse<T> {
    private T result;
    private JsonRpcResponseError error;
}

package com.exrates.inout.domain.neo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeoJsonRpcResponse<T> {
    private T result;
    private JsonRpcResponseError error;
}

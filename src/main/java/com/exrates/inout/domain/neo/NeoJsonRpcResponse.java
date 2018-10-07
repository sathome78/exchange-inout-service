package com.exrates.inout.domain.neo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeoJsonRpcResponse<T> {
    private T result;
    private JsonRpcResponseError error;
}

package com.exrates.inout.domain.qtum;

import com.exrates.inout.domain.JsonRpcResponseError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumJsonRpcResponseList<T> {
    private List<T> result;
    private JsonRpcResponseError error;
}

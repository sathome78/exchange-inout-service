package com.exrates.inout.domain.qtum;

import com.exrates.inout.domain.JsonRpcResponseError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumJsonRpcResponseList<T> {
    private List<T> result;
    private JsonRpcResponseError error;
}

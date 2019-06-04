package com.exrates.inout.service.usdx.model;

import lombok.Data;

@Data
public class UsdxApiResponse<T> {
    private T data;
    private String message;
    private String status;
}

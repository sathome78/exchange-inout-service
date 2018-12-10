package com.exrates.inout.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CacheData {

    private HttpServletRequest request;
    private String cacheKey;
    private Boolean forceUpdate;
}

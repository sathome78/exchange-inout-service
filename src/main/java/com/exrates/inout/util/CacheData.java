package com.exrates.inout.util;

import javax.servlet.http.HttpServletRequest;

public class CacheData {
    private HttpServletRequest request;
    private String cacheKey;
    private Boolean forceUpdate;
    /*constructor*/

    public CacheData(HttpServletRequest request, String cacheKey, Boolean forceUpdate) {
        this.request = request;
        this.cacheKey = cacheKey;
        this.forceUpdate = forceUpdate;
    }

    @Override
    public String toString() {
        return "CacheData{" +
                "request=" + request +
                ", cacheKey='" + cacheKey + '\'' +
                ", forceUpdate=" + forceUpdate +
                '}';
    }

    /*getters*/

    public HttpServletRequest getRequest() {
        return request;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public Boolean getForceUpdate() {
        return forceUpdate;
    }
}

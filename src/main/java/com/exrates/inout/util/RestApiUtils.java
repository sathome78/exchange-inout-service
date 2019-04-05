package com.exrates.inout.util;

import com.exrates.inout.exceptions.MissingBodyParamException;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.Map;

public class RestApiUtils {

    public static String decodePassword(String password, String keyString) {
        final byte[] txt = Base64.getDecoder().decode(password);
        final byte[] key = keyString.getBytes();
        final byte[] result = new byte[txt.length];
        for (int i = 0; i < txt.length; i++) {
            result[i] = (byte) (txt[i] ^ key[i % key.length]);
        }
        return new String(result);
    }

    public static String retrieveParamFormBody(Map<String, String> body, String paramName, boolean required) {
        String paramValue = body.get(paramName);
        if (required && StringUtils.isEmpty(paramValue)) {
            throw new MissingBodyParamException("Param " + paramName + " missing");
        }
        return paramValue;
    }

    public static String constructAbsoluteURI(String host, String port, String endpoint) {
        return String.join("", host, ":", port, endpoint);
    }
}

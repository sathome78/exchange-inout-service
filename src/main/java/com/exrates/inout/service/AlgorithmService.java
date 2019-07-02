package com.exrates.inout.service;


public interface AlgorithmService {

    String computeMD5Hash(String string);

    byte[] computeMD5Byte(String string);

    String sha1(String string);

    String sha256(String string);

    String base64Encode(String string);

    String base64Decode(String string);

    String getSecret(String сode);

    String encodeByKey(String code, String txt);

    String decodeByKey(String code, String text);
}

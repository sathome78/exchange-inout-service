package com.exrates.inout.service;


import com.exrates.inout.domain.enums.OperationType;

import java.math.BigDecimal;

public interface AlgorithmService {

    String computeMD5Hash(String string);

    byte[] computeMD5Byte(String string);

    String sha1(String string);

    String sha256(String string);

    String base64Encode(String string);

    String base64Decode(String string);

    BigDecimal computeAmount(BigDecimal amount, BigDecimal commission, OperationType type);

    BigDecimal computeCommission(BigDecimal amount, OperationType type);

    BigDecimal fromSatoshi(String amount);

    BigDecimal toBigDecimal(String value);
}

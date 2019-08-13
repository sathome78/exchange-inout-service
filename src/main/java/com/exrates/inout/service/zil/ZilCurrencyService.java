package com.exrates.inout.service.zil;

import com.exrates.inout.domain.dto.RefillRequestAddressDto;

import java.math.BigDecimal;

public interface ZilCurrencyService {
    String generatePrivateKey();

    String getPublicKeyFromPrivateKey(String privKey);

    String getAddressFromPrivateKey(String privKey);

    void createTransaction(RefillRequestAddressDto dto) throws Exception;

    BigDecimal getAmount(String address) throws Exception;

    BigDecimal getFee();

    BigDecimal scaleAmountToZilFormat(BigDecimal amount);
}

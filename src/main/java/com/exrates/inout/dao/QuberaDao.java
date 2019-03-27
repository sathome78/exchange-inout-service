package com.exrates.inout.dao;


import com.exrates.inout.domain.dto.QuberaRequestDto;

import java.util.Map;

public interface QuberaDao {

    Integer findUserIdByAccountNumber(String accountNumber);

    boolean logResponse(QuberaRequestDto requestDto);

    boolean saveUserDetails(int userId, int currencyId, String accountNumber, String iban);

    Map<String, String> getUserDetailsForCurrency(int userId, int currencyId);
}

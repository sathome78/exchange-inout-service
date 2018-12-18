package com.exrates.inout.dao;


import com.exrates.inout.domain.main.Payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface YandexMoneyMerchantDao {

    List<String> getAllTokens();

    String getTokenByUserEmail(String email);

    boolean createToken(String token, int userId);

    boolean deleteTokenByUserEmail(String userEmail);

    boolean updateTokenByUserEmail(String userEmail, String newToken);

    int savePayment(Integer currencyId, BigDecimal amount);

    Optional<Payment> getPaymentById(Integer id);

    void deletePayment(Integer id);
}

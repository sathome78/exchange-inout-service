package com.exrates.inout.service;

import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.yandex.money.api.methods.RequestPayment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Denis Savin (pilgrimm333@gmail.com)
 */
@Service
public interface YandexMoneyService extends IRefillable, IWithdrawable {

    List<String> getAllTokens();

    String getTokenByUserEmail(String userEmail);

    boolean addToken(String token, String email);

    boolean updateTokenByUserEmail(String newToken, String email);

    boolean deleteTokenByUserEmail(String email);

    String getTemporaryAuthCode(String redirectURI);

    String getTemporaryAuthCode();

    Optional<String> getAccessToken(String code);

    Optional<RequestPayment> requestPayment(String token, CreditsOperation creditsOperation);

    int saveInputPayment(Payment payment);

    Optional<Payment> getPaymentById(Integer id);

    void deletePayment(Integer id);

  @Override
  default Boolean createdRefillRequestRecordNeeded() {
    return null;
  }

  @Override
  default Boolean needToCreateRefillRequestRecord() {
    return null;
  }

  @Override
  default Boolean toMainAccountTransferringConfirmNeeded() {
    return null;
  }

  @Override
  default Boolean generatingAdditionalRefillAddressAvailable() {
    return null;
  }

  @Override
  default Boolean additionalTagForWithdrawAddressIsUsed() {
    return false;
  }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

  @Override
  default Boolean withdrawTransferringConfirmNeeded() {
    return null;
  }
}
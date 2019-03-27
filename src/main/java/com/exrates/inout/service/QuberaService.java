package com.exrates.inout.service;


import com.exrates.inout.domain.dto.AccountCreateDto;
import com.exrates.inout.domain.dto.AccountQuberaResponseDto;
import com.exrates.inout.domain.dto.QuberaRequestDto;

public interface QuberaService extends IRefillable, IWithdrawable {

  @Override
  default Boolean createdRefillRequestRecordNeeded() {
    return false;
  }

  @Override
  default Boolean needToCreateRefillRequestRecord() {
    return true;
  }

  @Override
  default Boolean toMainAccountTransferringConfirmNeeded() {
    return false;
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
  default Boolean withdrawTransferringConfirmNeeded() {
    return false;
  }

  @Override
  default Boolean additionalFieldForRefillIsUsed() {
    return false;
  }

  boolean logResponse(QuberaRequestDto requestDto);

  AccountQuberaResponseDto createAccount(AccountCreateDto accountCreateDto);
}

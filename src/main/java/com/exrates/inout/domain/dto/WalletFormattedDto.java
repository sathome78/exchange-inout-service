package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.Wallet;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalletFormattedDto {
  private Integer id;
  private String name;
  private BigDecimal totalInput;
  private BigDecimal totalOutput;
  private BigDecimal totalSell;
  private BigDecimal totalBuy;
  private BigDecimal reserveOrders;
  private BigDecimal reserveWithdraw;
  private BigDecimal activeBalance;
  private BigDecimal reservedBalance;

  public WalletFormattedDto() {
  }

  public WalletFormattedDto(Wallet wallet) {
    this.id = wallet.getId();
    this.name = wallet.getName();
    this.activeBalance = wallet.getActiveBalance();
    this.reservedBalance = wallet.getReservedBalance();
  }
}

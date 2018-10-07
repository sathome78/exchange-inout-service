package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.Wallet;
import lombok.Data;

import java.math.BigDecimal;

@Data
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

  public WalletFormattedDto(Wallet wallet) {
    this.id = wallet.getId();
    this.name = wallet.getName();
    this.activeBalance = wallet.getActiveBalance();
    this.reservedBalance = wallet.getReservedBalance();
  }
}

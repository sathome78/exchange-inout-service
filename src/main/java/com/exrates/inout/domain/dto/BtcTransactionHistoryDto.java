package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.serializer.LocalDateTimeSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;

@Data
@NoArgsConstructor
public class BtcTransactionHistoryDto {
  private String txId;
  private String address;
  private String category;
  private String amount;
  private String blockhash;
  private String fee;
  private Integer confirmations;
  @JsonSerialize(using = LocalDateTimeSerializer.class)
  private LocalDateTime time;

  public BtcTransactionHistoryDto(String txId, String address, String category, String amount, Integer confirmations, LocalDateTime time) {
    this.txId = txId;
    this.address = address;
    this.category = category;
    this.amount = amount;
    this.confirmations = confirmations;
    this.time = time;
  }

  public static Comparator<BtcTransactionHistoryDto> getComparator(String orderColumn, DataTableParams.OrderDirection orderDirection){
    int sortOrder = (orderDirection == DataTableParams.OrderDirection.ASC) ? 1 : -1;
    switch (orderColumn) {
      case "txId":
        return (ob1, ob2) -> compare(ob1.getTxId(), (ob2.getTxId())) * sortOrder;
      case "address":
        return (ob1, ob2) -> compare(ob1.getAddress(), (ob2.getAddress())) * sortOrder;
      case "category":
        return (ob1, ob2) -> compare(ob1.getCategory(), (ob2.getCategory())) * sortOrder;
      case "amount":
        return (ob1, ob2) -> compare(ob1.getAmount(), ob2.getAmount()) * sortOrder;
      case "blockhash":
        return (ob1, ob2) -> compare(ob1.getBlockhash(), ob2.getBlockhash()) * sortOrder;
      case "fee":
        return (ob1, ob2) -> compare(ob1.getFee(), ob2.getFee()) * sortOrder;
      case "confirmations":
        return (ob1, ob2) -> ob1.getConfirmations().compareTo(ob2.getConfirmations()) * sortOrder;
      case "time":
      default:
        return (ob1, ob2) -> ob1.getTime().compareTo(ob2.getTime()) * sortOrder;
    }
  }

  private static int compare(String ob1, String ob2){
    ob1 = ob1 == null ? "" : ob1;
    ob2 = ob2 == null ? "" : ob2;
    return ob1.compareTo(ob2);
  }

  public BtcTransactionHistoryDto(String txId) {
    this.txId = txId;
  }
}

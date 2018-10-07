package com.exrates.inout.domain.qtum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumTransaction {
    private String txid;
    private String category;
    private List<String> walletconflicts;
    private Integer confirmations;
    private String blockhash;
    private Double amount;
    private String address;
    private boolean trusted = true;
    private Integer vout;

}

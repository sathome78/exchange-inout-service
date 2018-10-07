package com.exrates.inout.domain.qtum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumListTransactions {

    private List<QtumTransaction> transactions;
    private String lastblock;

}

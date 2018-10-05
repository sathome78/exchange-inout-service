package com.exrates.inout.domain.qtum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumListTransactions {

    private List<QtumTransaction> transactions;
    private String lastblock;

}

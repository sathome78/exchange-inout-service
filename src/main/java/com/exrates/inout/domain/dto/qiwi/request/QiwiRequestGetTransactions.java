package com.exrates.inout.domain.dto.qiwi.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class QiwiRequestGetTransactions {
    private int start;
    private int limit;
}

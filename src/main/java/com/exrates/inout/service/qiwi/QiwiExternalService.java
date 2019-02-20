package com.exrates.inout.service.qiwi;

//exrates.model.dto.qiwi.response.QiwiResponseTransaction;

import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseTransaction;

import java.util.List;

public interface QiwiExternalService {
    String generateUniqMemo(int userId);

    List<QiwiResponseTransaction> getLastTransactions();

}

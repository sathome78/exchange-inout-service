package com.exrates.inout.dao;

import com.exrates.inout.domain.dto.MyInputOutputHistoryDto;
import com.exrates.inout.domain.other.PaginationWrapper;

import java.util.List;

public interface InputOutputDao {
    PaginationWrapper<List<MyInputOutputHistoryDto>> findUnconfirmedInvoices(Integer userId, Integer currencyId, Integer limit, Integer offset);
}

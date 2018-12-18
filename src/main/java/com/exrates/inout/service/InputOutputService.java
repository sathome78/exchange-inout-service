package com.exrates.inout.service;

import com.exrates.inout.domain.dto.MyInputOutputHistoryDto;
import com.exrates.inout.domain.enums.invoice.InvoiceOperationPermission;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Payment;
import com.exrates.inout.domain.other.PaginationWrapper;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface InputOutputService {

    PaginationWrapper<List<MyInputOutputHistoryDto>> findUnconfirmedInvoices(String userEmail, String currencyName, Integer limit, Integer offset, Locale locale);

    List generateAndGetButtonsSet(InvoiceStatus status, InvoiceOperationPermission permittedOperation, boolean authorisedUserIsHolder, Locale locale);

    Optional<CreditsOperation> prepareCreditsOperation(Payment payment, String userEmail, Locale locale);
}

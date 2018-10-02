package com.exrates.inout.service.impl;

import com.exrates.inout.domain.enums.OrderActionEnum;
import com.exrates.inout.domain.enums.OrderStatus;
import com.exrates.inout.domain.enums.invoice.InvoiceActionTypeEnum;
import com.exrates.inout.domain.enums.invoice.InvoiceStatus;
import org.springframework.stereotype.Component;

@Component
public class TransactionDescription {
    public String get(InvoiceStatus currentStatus, InvoiceActionTypeEnum action) {
        String currentStatusName = currentStatus == null ? "" : currentStatus.name();
        String actionName = action == null ? "" : action.name();
        return generate(currentStatusName, actionName);
    }

    public String get(OrderStatus currentStatus, OrderActionEnum action) {
        String currentStatusName = currentStatus == null ? "" : currentStatus.name();
        String actionName = action == null ? "" : action.name();
        return generate(currentStatusName, actionName);
    }

    private String generate(String currentStatus, String action) {
        return currentStatus.concat("::").concat(action);
    }

}

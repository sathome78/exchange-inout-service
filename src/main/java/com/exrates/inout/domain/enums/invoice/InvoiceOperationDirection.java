package com.exrates.inout.domain.enums.invoice;

import com.exrates.inout.exceptions.UnsupportedInvoiceOperationDirectionException;
import lombok.ToString;

import java.util.stream.Stream;

/**
 * Created by OLEG on 28.02.2017.
 */
@ToString
public enum InvoiceOperationDirection {
    REFILL(1), WITHDRAW(2), TRANSFER_VOUCHER(3);

    private int id;

    InvoiceOperationDirection(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static InvoiceOperationDirection convert(int id) {
        return Stream.of(InvoiceOperationDirection.values()).filter(item -> item.id == id).findFirst()
                .orElseThrow(() -> new UnsupportedInvoiceOperationDirectionException(String.format("id: %s", id)));
    }

    @Override
    public String toString() {
        return this.name();
    }
}

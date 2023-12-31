package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Objects;

@Data
@AllArgsConstructor
public class BtcTxOutputDto {
    private final String txId;
    private final Integer vout;

    // EQUALS AND HASH CODE -- IMPORTANT!!!


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BtcTxOutputDto that = (BtcTxOutputDto) o;
        return Objects.equals(txId, that.txId) &&
                Objects.equals(vout, that.vout);
    }

    @Override
    public int hashCode() {

        return Objects.hash(txId, vout);
    }
}

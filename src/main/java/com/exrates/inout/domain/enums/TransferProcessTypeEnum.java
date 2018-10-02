package com.exrates.inout.domain.enums;


import com.exrates.inout.exceptions.UnsupportedTransferProcessTypeException;
import com.exrates.inout.exceptions.UnsupportedTransferProcessTypeIdException;
import lombok.extern.log4j.Log4j2;

import java.util.Arrays;

@Log4j2
public enum TransferProcessTypeEnum {
    TRANSFER(1),
    VOUCHER(2),
    VOUCHER_FREE(3);

    private Integer code;

    TransferProcessTypeEnum(Integer code) {
        this.code = code;
    }

    public static TransferProcessTypeEnum convert(TransferProcessTypeEnum transferProcessTypeEnum) {
        return convert(transferProcessTypeEnum.getCode());
    }

    public static TransferProcessTypeEnum convert(int id) {
        return Arrays.stream(TransferProcessTypeEnum.class.getEnumConstants())
                .filter(e -> e.code == id)
                .findAny()
                .orElseThrow(() -> new UnsupportedTransferProcessTypeIdException(String.valueOf(id)));
    }

    public static TransferProcessTypeEnum convert(String name) {
        return Arrays.stream(TransferProcessTypeEnum.class.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny()
                .orElseThrow(() -> new UnsupportedTransferProcessTypeException(name));
    }

    public Integer getCode() {
        return code;
    }
}

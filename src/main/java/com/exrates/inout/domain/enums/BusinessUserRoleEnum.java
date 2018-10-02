package com.exrates.inout.domain.enums;

import com.exrates.inout.exceptions.UnsupportedBusinessUserRoleNameException;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
public enum BusinessUserRoleEnum {
    ADMIN,
    USER,
    EXCHANGE,
    VIP_USER,
    TRADER,
    BOT,
    MARKET_MAKER;


    public static BusinessUserRoleEnum convert(String name) {
        return Arrays.stream(BusinessUserRoleEnum.class.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny()
                .orElseThrow(() -> new UnsupportedBusinessUserRoleNameException(name));
    }
}

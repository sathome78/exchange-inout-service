package com.exrates.inout.domain.enums;

import com.exrates.inout.exceptions.UnsupportedGroupUserRoleNameException;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@NoArgsConstructor
public enum GroupUserRoleEnum {
    ADMINS,
    USERS,
    BOT;

    public static GroupUserRoleEnum convert(String name) {
        return Arrays.stream(GroupUserRoleEnum.class.getEnumConstants())
                .filter(e -> e.name().equals(name))
                .findAny()
                .orElseThrow(() -> new UnsupportedGroupUserRoleNameException(name));
    }
}

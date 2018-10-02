package com.exrates.inout.domain.enums;

import java.util.Arrays;

public enum UserRole implements RealCheckableRole {

    ADMINISTRATOR(1),
    ACCOUNTANT(2),
    ADMIN_USER(3),
    USER(4),
    ROLE_CHANGE_PASSWORD(5),
    EXCHANGE(6),
    VIP_USER(7),
    TRADER(8),
    FIN_OPERATOR(9),
    BOT_TRADER(10),
    ICO_MARKET_MAKER(11);

    private final int role;

    UserRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public static UserRole convert(int id) {
        return Arrays.stream(UserRole.class.getEnumConstants())
                .filter(e -> e.role == id)
                .findAny().orElse(USER)
                /*.orElseThrow(() -> new UnsupportedUserRoleIdException(String.valueOf(id)))*/;
    }

    public String toString() {
        return this.name();
    }

    public String getName() {
        return this.name();
    }
}
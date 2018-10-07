package com.exrates.inout.domain.main;

import lombok.Data;

@Data
public class ClientBank {
    private Integer id;
    private Integer currencyId;
    private String name;
    private String code;
}

package com.exrates.inout.domain.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class Currency {

    private int id;
    private String name;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;

    public Currency(int id) {
        this.id = id;
    }

}

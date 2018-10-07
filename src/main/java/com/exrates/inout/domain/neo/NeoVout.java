package com.exrates.inout.domain.neo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeoVout {
    private Integer n;
    private String asset;
    private String value;
    private String address;
}

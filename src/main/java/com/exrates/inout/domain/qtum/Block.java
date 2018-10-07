package com.exrates.inout.domain.qtum;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
    private String hash;
    private Long time;
    private Integer height;
    private List<String> tx;
    private Integer confirmations;
}

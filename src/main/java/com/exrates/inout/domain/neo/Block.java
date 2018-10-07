package com.exrates.inout.domain.neo;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Block {
    private String hash;
    private Long time;
    private Integer index;
    private List<NeoTransaction> tx;
    private Integer confirmations;
}

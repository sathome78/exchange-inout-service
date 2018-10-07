package com.exrates.inout.domain.neo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NeoTransaction {
    private String txid;
    private String type;
    private List<NeoVout> vout;
    private Integer confirmations;
    private String blockhash;
}


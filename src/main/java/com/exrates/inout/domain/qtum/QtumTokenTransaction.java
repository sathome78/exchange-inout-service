package com.exrates.inout.domain.qtum;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QtumTokenTransaction {
    private String blockHash;
    private Integer blockNumber;
    private String transactionHash;
    private String from;
    private String to;
    private String contractAddress;
    private List<QtumLogDto> log;
}

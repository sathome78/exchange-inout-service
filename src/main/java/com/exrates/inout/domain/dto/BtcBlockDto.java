package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BtcBlockDto {
    private String hash;
    private Integer height;
    private Long time;
}

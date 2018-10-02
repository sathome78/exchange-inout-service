package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class BtcBlockDto {
    private String hash;
    private Integer height;
    private Long time;
}

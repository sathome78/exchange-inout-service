package com.exrates.inout.domain.main;

import com.exrates.inout.domain.enums.TokenType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TemporalToken {

    private int id;
    private String value;
    private int userId;
    private boolean expired;
    private LocalDateTime dateCreation;
    private TokenType tokenType;
    private String checkIp;
    private boolean isAlreadyUsed;

}

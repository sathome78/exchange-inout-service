package com.exrates.inout.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Data
public class UserGroupBalanceDto {
    private int curId;
    private String currency;
    private ReportGroupUserRole reportGroupUserRole;
    private BigDecimal totalBalance;

    public UserGroupBalanceDto.CurAndId getCurAndId(){return new UserGroupBalanceDto.CurAndId(curId, currency);}

    //wolper 19.04.18
    //static class for wrapping a  tuple
    @EqualsAndHashCode(of = {"id"})
    @Getter
    @AllArgsConstructor
    public class CurAndId{
        private int id;
        private String currency;
    }
}

package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.main.Payment;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Data
@NoArgsConstructor
public class PrepareCreditsOperationDto {
    Payment payment;
    int userId;
    Locale locale;
}

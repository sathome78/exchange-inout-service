package com.exrates.inout.controller;

import com.exrates.inout.domain.dto.PrepareCreditsOperationDto;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.service.InputOutputService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/api")
@RequiredArgsConstructor
public class ApiController {

    private final InputOutputService inputOutputService;

    @PostMapping("/prepareCreditsOperation")
    public Optional<CreditsOperation> prepareCreditsOperation(PrepareCreditsOperationDto dto){
        return inputOutputService.prepareCreditsOperation(dto.getPayment(), dto.getUserId(), dto.getLocale());
    }
}

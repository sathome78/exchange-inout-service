package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.merchant.TransferVoucherFreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Service
public class TransferVoucherFreeServiceImpl implements TransferVoucherFreeService {

  @Autowired
  private AlgorithmService algorithmService;

  public Map<String, String> transfer(TransferRequestCreateDto transferRequestCreateDto) {
    String hash = algorithmService.sha256(new StringJoiner(":")
        .add(transferRequestCreateDto.getId().toString())
        .add(transferRequestCreateDto.getAmount().toString())
        .add(transferRequestCreateDto.getCurrencyName())
        .add(transferRequestCreateDto.getRecipient())
        .toString()
        .toUpperCase());
    return new HashMap<String, String>() {{
      put("hash", hash);
    }};
  }

}
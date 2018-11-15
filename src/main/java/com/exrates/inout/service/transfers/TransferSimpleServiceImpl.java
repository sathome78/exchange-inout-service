package com.exrates.inout.service.transfers;

import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.service.merchant.TransferSimpleService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TransferSimpleServiceImpl implements TransferSimpleService {

  public Map<String, String> transfer(TransferRequestCreateDto transferRequestCreateDto) {
    return new HashMap<>();
  }

}

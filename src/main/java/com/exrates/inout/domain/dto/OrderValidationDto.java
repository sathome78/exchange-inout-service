package com.exrates.inout.domain.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class OrderValidationDto {
  private Map<String, Object> errors = new HashMap<>();
  private Map<String, Object[]> errorParams = new HashMap<>();
}

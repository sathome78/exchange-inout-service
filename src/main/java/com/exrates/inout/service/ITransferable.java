package com.exrates.inout.service;

import com.exrates.inout.domain.dto.TransferRequestCreateDto;
import com.exrates.inout.domain.enums.TransferProcessTypeEnum;

import java.util.Map;

public interface ITransferable extends IMerchantService {

    Map<String, String> transfer(TransferRequestCreateDto transferRequestCreateDto);

    Boolean isVoucher();

    Boolean recipientUserIsNeeded();

    TransferProcessTypeEnum processType();
}

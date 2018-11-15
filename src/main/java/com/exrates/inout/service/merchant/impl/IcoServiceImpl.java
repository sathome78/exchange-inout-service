package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.merchant.IcoService;
import com.exrates.inout.service.utils.WithdrawUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class IcoServiceImpl implements IcoService {

    @Autowired
    private WithdrawUtils withdrawUtils;

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        throw new RuntimeException("not implemented");
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        throw new RuntimeException("not implemented");
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        throw new RuntimeException("not implemented");
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }


}

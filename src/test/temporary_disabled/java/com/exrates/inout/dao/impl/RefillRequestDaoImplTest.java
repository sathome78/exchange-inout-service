package com.exrates.inout.dao.impl;

import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class RefillRequestDaoImplTest extends InoutTestApplication {

    @Autowired
    private RefillRequestDao refillRequestDao;

    @Test
    public void checkInputRequests() {
        User user = registerNewUser();

        RefillRequestCreateDto refillRequestCreateDto = new RefillRequestCreateDto();
        refillRequestCreateDto.setUserId(user.getId());
        refillRequestCreateDto.setMerchantId(aunitMerchant.getId());
        refillRequestCreateDto.setCurrencyId(aunitCurrency.getId());
        refillRequestCreateDto.setAmount(new BigDecimal(10));
        refillRequestCreateDto.setStatus(RefillStatusEnum.ACCEPTED_AUTO);
        refillRequestCreateDto.setCommissionId(6);
        refillRequestCreateDto.setNeedToCreateRefillRequestRecord(true);
        refillRequestCreateDto.setAddress("test");

        refillRequestDao.storeRefillRequestAddress(refillRequestCreateDto);
        refillRequestDao.create(refillRequestCreateDto);
    }
}
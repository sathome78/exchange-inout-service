package com.exrates.inout.service.impl;


import com.exrates.inout.InoutTestApplication;
import com.exrates.inout.controller.WithdrawRequestController;
import com.exrates.inout.dao.WithdrawRequestDao;
import com.exrates.inout.domain.dto.CommissionDataDto;
import com.exrates.inout.domain.dto.WithdrawRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawRequestParamsDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.dto.TestUser;
import com.exrates.inout.service.CommissionService;
import com.exrates.inout.service.WalletService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;

public class WithdrawTest extends InoutTestApplication {

    @Autowired
    private WithdrawRequestController withdrawRequestController;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private WithdrawRequestDao withdrawRequestDao;

    @MockBean
    private WalletService walletService;

    @Test
    public void withdraw(){
        final String DESTINATION_ADDRESS = "destination address";
        final BigDecimal amountToWithdraw = new BigDecimal("10");

        Principal principal = Mockito.mock(Principal.class);
        TestUser sender = registerNewUser();
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        Mockito.when(principal.getName()).thenReturn(sender.getEmail());
        Mockito.when(servletRequest.getUserPrincipal()).thenReturn(principal);
        Mockito.when(servletRequest.getAttribute(any())).thenReturn(new Locale("en"));

        WithdrawRequestParamsDto params = new WithdrawRequestParamsDto();
        params.setCurrency(aunitCurrency.getId());
        params.setMerchant(aunitMerchant.getId());
        params.setDestination(DESTINATION_ADDRESS);
        params.setOperationType(OperationType.OUTPUT);
        params.setDestinationTag(StringUtils.EMPTY);
        params.setMerchantImage(1);
        params.setSum(amountToWithdraw);

        Mockito.when(walletService.findByUserAndCurrency(any(), any())).thenReturn(new Wallet(aunitCurrency.getId(), null, null));
        Mockito.when(walletService.ifEnoughMoney(anyInt(), any())).thenReturn(true);
        Mockito.when(walletService.getWalletABalance(anyInt())).thenReturn(amountToWithdraw);
        Mockito.when(walletService.walletInnerTransfer(anyInt(), any(), eq(TransactionSourceType.WITHDRAW), anyInt(), anyString())).thenReturn(WalletTransferStatus.SUCCESS);

        Map<String, String> result = withdrawRequestController.createWithdrawalRequest(params, principal, servletRequest, new Locale("en"));

        List<WithdrawRequestFlatDto> requests = withdrawRequestDao.findByUserId(sender.getId());
        assertEquals(1, requests.size());
        WithdrawRequestFlatDto withdrawRequest = requests.get(0);
        assertTrue(compareObjects(amountToWithdraw, withdrawRequest.getAmount()));

        CommissionDataDto commissionDataDto = commissionService.normalizeAmountAndCalculateCommission(sender.getId(), amountToWithdraw, OperationType.OUTPUT, aunitCurrency.getId(), aunitMerchant.getId(), StringUtils.EMPTY);
        assertTrue(compareObjects(commissionDataDto.getTotalCommissionAmount(), withdrawRequest.getCommissionAmount()));

        assertEquals(DESTINATION_ADDRESS, withdrawRequest.getWallet());
    }

}

package com.exrates.inout.service.jobs.invoice;

import com.exrates.inout.domain.dto.WithdrawRequestFlatDto;
import com.exrates.inout.domain.enums.invoice.WithdrawStatusEnum;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RippleCheckConsensusException;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.WithdrawService;
import com.exrates.inout.service.ripple.RippleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
public class RippleJobs {

    private static final String XRP_MERCHANT = "Ripple";

    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private RippleService rippleService;

    private final static ExecutorService ordersExecutors = Executors.newSingleThreadExecutor();

    @Scheduled(initialDelay = 180000, fixedDelay = 1000 * 60 * 5)
    private void checkWithdrawals() {
        Merchant merchant = merchantService.findByName(XRP_MERCHANT);
        List<WithdrawRequestFlatDto> dtos = withdrawService.getRequestsByMerchantIdAndStatus(merchant.getId(),
                Collections.singletonList(WithdrawStatusEnum.ON_BCH_EXAM.getCode()));
        if (dtos != null && !dtos.isEmpty()) {
            dtos.forEach(p -> ordersExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    checkWithdraw(p.getId(), p.getTransactionHash(), p.getAdditionalParams());
                }
            }));
        }
    }

    private void checkWithdraw(int id, String hash, String additionalParams) {
        try {
            boolean checked = rippleService.checkSendedTransaction(hash, additionalParams);
            if (checked) {
                withdrawService.finalizePostWithdrawalRequest(id);
            }
        } catch (RippleCheckConsensusException e) {
            log.error("xrp transaction validation error " + e);
            withdrawService.rejectToReview(id);
        } catch (Exception e) {
            log.error("xrp transaction check error, will check it next time " + e);
        }
    }
}

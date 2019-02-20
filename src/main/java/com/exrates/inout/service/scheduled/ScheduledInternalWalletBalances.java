package com.exrates.inout.service.scheduled;

//exrates.service.WalletService;

import com.exrates.inout.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@PropertySource(value = {"classpath:/scheduler.properties"})
@Service
public class ScheduledInternalWalletBalances {

    private final WalletService walletService;

    @Autowired
    public ScheduledInternalWalletBalances(WalletService walletService) {
        this.walletService = walletService;
    }

    @Scheduled(cron = "${scheduled.update.internal-balances}")
    public void update() {
        walletService.updateInternalWalletBalances();
    }
}

package com.exrates.inout.service.scheduled;

import me.exrates.service.MerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@PropertySource(value = {"classpath:/scheduler.properties"})
@Service
public class ScheduledMerchantCommissionsLimits {

    private final MerchantService merchantService;

    @Autowired
    public ScheduledMerchantCommissionsLimits(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @Scheduled(cron = "${scheduled.update.withdraw-commissions-limits}")
    public void update() {
        merchantService.updateMerchantCommissionsLimits();
    }
}
package com.exrates.inout.service.job.bot;

import lombok.extern.log4j.Log4j2;
import me.exrates.model.enums.OrderType;
import me.exrates.service.BotService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@DisallowConcurrentExecution
@Log4j2(topic = "bot_trader")
public class BotCreateOrderJob implements Job {

    @Autowired
    private BotService botService;

    public BotCreateOrderJob() {
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
        log.debug("BotCreateOrderJob execute(), start job");
        Integer currencyPairId = jobExecutionContext.getMergedJobDataMap().getInt("currencyPairId");
        OrderType orderType = OrderType.valueOf(jobExecutionContext.getMergedJobDataMap().getString("orderType"));
        log.debug("Running sequence for CurrencyPairId {}, orderType {}", currencyPairId, orderType.name());
        botService.runOrderCreation(currencyPairId, orderType);
    }


}

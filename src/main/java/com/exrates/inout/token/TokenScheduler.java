package com.exrates.inout.token;

import com.exrates.inout.domain.main.TemporalToken;
import com.exrates.inout.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class TokenScheduler {
    private static final Logger LOGGER = LogManager.getLogger(TokenScheduler.class);
    public static final String TRIGGER_GROUP = "token";
    private static final Integer TOKEN_LIFE_TIME_DAYS = 1;

    private Scheduler scheduler = null;

    @Autowired
    protected UserService userService;

    @PostConstruct
    private void init() {
        try {
            scheduler = new StdSchedulerFactory().getScheduler();
            scheduler.start();
            LOGGER.debug("TokenScheduler is started ");
        } catch (SchedulerException ex) {
            LOGGER.error("error while TokenScheduler init " + ex.getLocalizedMessage());
        }
    }

    @PreDestroy
    private void destroy() {
        try {
            scheduler.shutdown(true);
            LOGGER.debug("TokenScheduler is stoped");
        } catch (SchedulerException ex) {
            LOGGER.error("error while TokenScheduler destroy " + ex.getLocalizedMessage());
        }
    }

    public void initTrigers() {
        List<JobKey> jobsInQueue = getAllJobKeys();
        int jobsInQueueCount = jobsInQueue == null ? 0 : jobsInQueue.size();
        List<TemporalToken> tokens = userService.getAllTokens();
        try {
            for (TemporalToken token : tokens) {
                Trigger trigger = scheduler.getTrigger(new TriggerKey(String.valueOf(token.getId()), TRIGGER_GROUP));
                if (trigger == null) {
                    Date startDate = Date.from(token.getDateCreation()
                            .plusDays(TOKEN_LIFE_TIME_DAYS).atZone(ZoneId.systemDefault()).toInstant());
                    trigger = TriggerBuilder.newTrigger()
                            .withIdentity(String.valueOf(token.getId()), TRIGGER_GROUP)
                            .startAt(startDate)
                            .withSchedule(
                                    SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionFireNow()
                            )
                            .build();
                    JobDetail job = JobBuilder.newJob(ClearTokenJob.class)
                            .withIdentity(String.valueOf(token.getId() + ":" + token.getUserId() + "::" + token.getTokenType().getTokenType()), TRIGGER_GROUP)
                            .usingJobData("tokenId", token.getId())
                            .usingJobData("tokenValue", token.getValue())
                            .usingJobData("tokenUser", token.getUserId())
                            .usingJobData("tokenDateCreation", token.getDateCreation().toString())
                            .build();

                    scheduler.scheduleJob(job, trigger);
                }
            }
            LOGGER.debug(String.format("expired token scheduler: queued %s job(s)" + "\n" + "  in queue now %s jobs : %s",
                    (scheduler.getJobKeys(GroupMatcher.jobGroupEquals(TRIGGER_GROUP)).size() - jobsInQueueCount),
                    scheduler.getJobKeys(GroupMatcher.jobGroupEquals(TRIGGER_GROUP)).size(),
                    getAllJobKeys()));
        } catch (SchedulerException ex) {
            LOGGER.error("error while token clean triggers init " + ex.getLocalizedMessage());
        }
        getAllJobKeys();
    }

    private List<JobKey> getAllJobKeys() {
        List<JobKey> jobs;
        try {
            jobs = new ArrayList<>(scheduler.getJobKeys(GroupMatcher.groupEquals(TRIGGER_GROUP)));
        } catch (SchedulerException ex) {
            LOGGER.error("error while token jobs list retrieving: " + ex.getLocalizedMessage());
            jobs = null;
        }
        return jobs;
    }

    @PreDestroy
    public void shutdown() {
        if (scheduler != null) {
            try {
                scheduler.shutdown();
            } catch (SchedulerException ex) {
                LOGGER.error(ex);
            }
        }
    }
}

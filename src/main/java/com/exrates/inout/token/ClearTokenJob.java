package com.exrates.inout.token;

import com.exrates.inout.exceptions.handlers.RestResponseErrorHandler;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ClearTokenJob implements Job {

    private static final Logger LOGGER = LogManager.getLogger(TokenScheduler.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDataMap jobParams = jobExecutionContext
                .getJobDetail()
                .getJobDataMap();
        String message;
        String tokenString = "tokenId: " + jobParams.get("tokenId")
                + " userId: " + jobParams.get("tokenUser")
                + " : " + jobParams.get("tokenDateCreation");

        try {
            if (false/*TokenScheduler.getTokenScheduler().deleteExpiredToken(jobParams.getString("tokenValue"))*/ /*todo repair fk constrins for users delete job*/) {
                message = String.format("the expired token was deleted: %s" + "\n" + "  in queue now %s jobs remain",
                        tokenString, jobExecutionContext.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(TokenScheduler.TRIGGER_GROUP)).size() - 1);
            } else {
                message = String.format("the expired token was NOT deleted: %s" + "\n" + "  in queue now %s jobs remain",
                        tokenString, jobExecutionContext.getScheduler().getJobKeys(GroupMatcher.jobGroupEquals(TokenScheduler.TRIGGER_GROUP)).size() - 1);
            }
            LOGGER.debug(message);
        } catch (Exception e) {
            LOGGER.error(String.format("error while job executing: %s %s", tokenString, e.getLocalizedMessage()));
        }
    }
}

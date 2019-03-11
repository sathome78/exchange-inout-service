package com.exrates.inout.service;

import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagement;
import com.amazonaws.services.simplesystemsmanagement.AWSSimpleSystemsManagementClientBuilder;
import com.amazonaws.services.simplesystemsmanagement.model.GetParameterRequest;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SSMGetterImpl implements SSMGetter {
    AWSSimpleSystemsManagement awsSimpleSystemsManagement = AWSSimpleSystemsManagementClientBuilder.defaultClient();
    private static Logger logger = LoggerFactory.getLogger(SSMGetterImpl.class);
    private LoadingCache<String, String> customerCache;

    public SSMGetterImpl() {
        this.customerCache = CacheBuilder.newBuilder().maximumSize(10L).expireAfterWrite(5L, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
            public String load(String id) {
                SSMGetterImpl.logger.info("Lookup ssm");
                return SSMGetterImpl.this.lookupSSM(id);
            }
        });
    }

    public String lookup(String password) {
        try {
            return (String)this.customerCache.get(password);
        } catch (ExecutionException var3) {
            logger.error(MessageFormat.format("Ssm is missing value, Error message {0}", var3.getMessage()), var3);
            throw new RuntimeException(var3);
        }
    }

    private String lookupSSM(String key) {
        logger.info("Getting value from ssm for key" + key);
        GetParameterRequest getParameterRequest = (new GetParameterRequest()).withName(key).withWithDecryption(Boolean.TRUE);

        try {
            return this.awsSimpleSystemsManagement.getParameter(getParameterRequest).getParameter().getValue();
        } catch (RuntimeException var4) {
            logger.error(MessageFormat.format("SSM is missing value for key {0}", key));
            logger.error(MessageFormat.format("error message {0}", var4.getMessage()), var4);
            throw var4;
        }
    }
}
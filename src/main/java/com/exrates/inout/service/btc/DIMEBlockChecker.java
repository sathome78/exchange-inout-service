package com.exrates.inout.service.btc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;

@Service("dimeBlockChecker")
@PropertySource("classpath:/coins_api_endpoints.properties")
public class DIMEBlockChecker extends APIExplorer {

    @Autowired
    Client client;

    public DIMEBlockChecker(@Value("#{dime.blocks.endpoint}") String endpoint, Client client) {
        super(endpoint, client);
    }
}
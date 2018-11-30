package com.exrates.inout.service.btc;

import javax.ws.rs.client.Client;

public class APIExplorer implements BitcoinBlocksCheckerService {

    private final String endpoint;

    private final Client client;

    public APIExplorer(String endpoint, Client client) {
        this.endpoint = endpoint;
        this.client = client;
    }

    @Override
    public long getExplorerBlocksAmount() {
        return client.target(endpoint).request().get().readEntity(Long.class);
    }
}

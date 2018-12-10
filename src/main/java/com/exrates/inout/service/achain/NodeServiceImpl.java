package com.exrates.inout.service.achain;

import com.exrates.inout.properties.CryptoCurrencyProperties;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Log4j2(topic = "achain")
@Component
public class NodeServiceImpl implements NodeService {

    private final SDKHttpClient httpClient;

    @Autowired
    private CryptoCurrencyProperties ccp;

    @Autowired
    public NodeServiceImpl(SDKHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String getMainAccountAddress() {
        return ccp.getOtherCoins().getAchain().getMainAddress();
    }

    @Override
    public String getAccountName() {
        return ccp.getOtherCoins().getAchain().getAccountName();
    }

    @Override
    public long getBlockCount() {
        log.debug("NodeServiceImpl|getBlockCount");
        String result = httpClient.post(ccp.getOtherCoins().getAchain().getUrl(), ccp.getOtherCoins().getAchain().getRpcUser(), "blockchain_get_block_count", new JSONArray());
        JSONObject createTaskJson = new JSONObject(result);
        return createTaskJson.getLong("result");
    }

    @Override
    public JSONArray getBlock(long blockNum) {
        log.debug("NodeServiceImpl|getBlock [{}]", blockNum);
        String result = httpClient.post(ccp.getOtherCoins().getAchain().getUrl(), ccp.getOtherCoins().getAchain().getRpcUser(), "blockchain_get_block", String.valueOf(blockNum));
        JSONObject createTaskJson = new JSONObject(result);
        return createTaskJson.getJSONObject("result").getJSONArray("user_transaction_ids");
    }

    @Override
    public boolean getSyncState() {
        log.debug("NodeServiceImpl|getSyncState [{}]");
        String result = httpClient.post(ccp.getOtherCoins().getAchain().getUrl(), ccp.getOtherCoins().getAchain().getRpcUser(), "blockchain_is_synced", new JSONArray());
        JSONObject createTaskJson = new JSONObject(result);
        return createTaskJson.getBoolean("result");
    }

    @Override
    public JSONArray getBlockTransactions(long blockNum) {
        log.debug("NodeServiceImpl|getBlockTransactions [{}]", blockNum);
        String result = httpClient.post(ccp.getOtherCoins().getAchain().getUrl(), ccp.getOtherCoins().getAchain().getRpcUser(), "blockchain_get_block_transactions", String.valueOf(blockNum));
        JSONObject transactions = new JSONObject(result);
        return transactions.getJSONArray("result");
    }

    @Override
    public JSONObject getPrettyContractTransaction(String innerHash) {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put(innerHash);
        log.info("getPretty|[result_trx_id={}]",
                innerHash);
        String result = httpClient.post(ccp.getOtherCoins().getAchain().getUrl(), ccp.getOtherCoins().getAchain().getRpcUser(), "blockchain_get_pretty_contract_transaction", jsonArray);
        return new JSONObject(result);
    }
}

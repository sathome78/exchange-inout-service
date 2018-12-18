package com.exrates.inout.service.crypto_currencies;

import com.exrates.inout.dao.EDCAccountDao;
import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.InvalidAccountException;
import com.exrates.inout.exceptions.MerchantException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.EDCServiceNode;
import com.exrates.inout.service.TransactionService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2(topic = "edc_log")
@Service
public class EDCServiceNodeImpl implements EDCServiceNode {

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");
    private final String IMPORT_KEY = "{\"method\": \"import_key\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\",\"%s\"], \"id\": %s}";
    private final String TRANSFER_EDC = "{\"method\":\"transfer\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"true\"], \"id\":%s}";

    private final ExecutorService workers = Executors.newFixedThreadPool(2);
    private volatile boolean isRunning = true;
    private volatile boolean debugLog = true;

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    TransactionService transactionService;
    @Autowired
    EDCAccountDao edcAccountDao;

    @PreDestroy
    public void destroy() {
        isRunning = false;
        workers.shutdown();
    }

    @Override
    public void transferFromMainAccount(final String accountName, final String amount) throws IOException {
        final String responseImportKey = makeRpcCallFast(IMPORT_KEY, ccp.getOtherCoins().getEdc().getAccountMain(), ccp.getOtherCoins().getEdc().getAccountPrivateKey(), 1);
        if (responseImportKey.contains("true")) {
            final String responseTransfer = makeRpcCallFast(TRANSFER_EDC, ccp.getOtherCoins().getEdc().getAccountMain(), accountName, amount, "EDC", "Output transfer", 1);
            if (responseTransfer.contains("error")) {
                log.error(responseTransfer);
                if (responseTransfer.contains("rec && rec->name == account_name_or_id")) {
                    throw new InvalidAccountException();
                }
                if (responseTransfer.contains("Insufficient Balance")) {
                    throw new InsufficientCostsInWalletException();
                }
                throw new MerchantException(responseTransfer);
            }
        }
    }

    private String makeRpcCallFast(String rpc, Object... args) throws IOException {
        final String rpcCall = String.format(rpc, args);
        final Request request = new Request.Builder()
                .url(ccp.getOtherCoins().getEdc().getBlockchainHostFast())
                .post(RequestBody.create(MEDIA_TYPE, rpcCall))
                .build();
        return HTTP_CLIENT.newCall(request)
                .execute()
                .body()
                .string();
    }
}

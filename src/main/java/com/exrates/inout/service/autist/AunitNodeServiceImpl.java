package com.exrates.inout.service.autist;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.google.common.hash.Hashing;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.OnMessage;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.exrates.inout.service.autist.AunitServiceImpl.AUNIT_CURRENCY;
import static com.exrates.inout.service.autist.AunitServiceImpl.AUNIT_MERCHANT;
import static com.exrates.inout.service.autist.MemoDecryptor.decryptBTSmemo;


@Log4j2(topic = "aunit")
@ClientEndpoint
@Service
public class AunitNodeServiceImpl {

    private URI WS_SERVER_URL;
    private Session session;
    private volatile RemoteEndpoint.Basic endpoint = null;
    private Merchant merchant;
    private Currency currency;
    private String privateKey;

    private CryptoCurrencyProperties ccp;
    private MerchantSpecParamsDao merchantSpecParamsDao;
    private AunitService aunitService;
    private RefillService refillService;

    private int latIrreversableBlocknumber;
    private final String lastIrreversebleBlock = "last_irreversible_block_num";

    @Autowired
    public AunitNodeServiceImpl(CryptoCurrencyProperties ccp,
                                MerchantService merchantService,
                                CurrencyService currencyService,
                                MerchantSpecParamsDao merchantSpecParamsDao,
                                AunitService aunitService,
                                RefillService refillService) {
        try {
            this.ccp = ccp;
            this.merchant = merchantService.findByName(AUNIT_MERCHANT);
            this.currency = currencyService.findByName(AUNIT_CURRENCY);
            latIrreversableBlocknumber = Integer.valueOf(merchantSpecParamsDao.getByMerchantIdAndParamName(merchant.getId(), lastIrreversebleBlock).getParamValue());
            this.merchantSpecParamsDao = merchantSpecParamsDao;
            this.aunitService = aunitService;
            this.refillService = refillService;
            privateKey = merchantService.getPassMerchantProperties("AUNIT").getProperty("privateKey");
            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> {
                try {
                    reconnect();
                } catch (Exception e) {
                    log.info(e);
                }
            }, 0L, 3L, TimeUnit.MINUTES); //todo
        } catch (Exception e) {
            log.error("AUNIT not started: \n" + e.getMessage());
        }
    }

    @PostConstruct
    public void init() {
        WS_SERVER_URL = URI.create(ccp.getOtherCoins().getAunit().getWs());
        connectAndSubscribe();
    }

    private void connectAndSubscribe() {
        try {
            session = ContainerProvider.getWebSocketContainer()
                    .connectToServer(this, WS_SERVER_URL);
            session.setMaxBinaryMessageBufferSize(5012000);
            session.setMaxTextMessageBufferSize(5012000);
            session.setMaxIdleTimeout(Long.MAX_VALUE);

            endpoint = session.getBasicRemote();
            subscribeToTransactions();
        } catch (Exception e) {
            System.out.println("gabella");
            e.printStackTrace();
        }
    }

    private void reconnect() {
        if (!session.isOpen()) {
            try {
                init();
            } catch (Exception e) {
                log.error(e);
            }
        }
    }

    private void subscribeToTransactions() throws IOException {
        JSONObject login = new JSONObject();
        login.put("id", 0);
        login.put("method", "call");
        login.put("params", new JSONArray().put(1).put("login").put(new JSONArray().put("").put("")));

        JSONObject db = new JSONObject();
        db.put("id", 1);
        db.put("method", "call");
        db.put("params", new JSONArray().put(1).put("database").put(new JSONArray()));

        JSONObject netw = new JSONObject();
        netw.put("id", 2);
        netw.put("method", "call");
        netw.put("params", new JSONArray().put(1).put("network_broadcast").put(new JSONArray()));

        JSONObject history = new JSONObject();
        history.put("id", 3);
        history.put("method", "call");
        history.put("params", new JSONArray().put(1).put("history").put(new JSONArray()));

        JSONObject orders = new JSONObject();
        orders.put("id", 4);
        orders.put("method", "call");
        orders.put("params", new JSONArray().put(1).put("orders").put(new JSONArray()));

        JSONObject chainId = new JSONObject();
        chainId.put("id", 5);
        chainId.put("method", "call");
        chainId.put("params", new JSONArray().put(2).put("get_chain_id").put(new JSONArray().put(new JSONArray())));

        JSONObject get_object = new JSONObject();
        get_object.put("id", 6);
        get_object.put("method", "call");
        get_object.put("params", new JSONArray().put(2).put("get_objects").put(new JSONArray().put(new JSONArray().put("2.1.0"))));


        JSONObject subscribe = new JSONObject();
        subscribe.put("id", 7);
        subscribe.put("method", "call");
        subscribe.put("params", new JSONArray().put(2).put("set_subscribe_callback").put(new JSONArray().put(0).put(false)));

        endpoint.sendText(login.toString());
        endpoint.sendText(db.toString());
        endpoint.sendText(netw.toString());
        endpoint.sendText(history.toString());
        endpoint.sendText(orders.toString());
        endpoint.sendText(chainId.toString());
        endpoint.sendText(subscribe.toString());
        endpoint.sendText(get_object.toString());
    }

    @OnMessage()
    public void onMessage(String msg) {
        try {
            if (msg.contains("notice")) setIrreversableBlock(msg);
            else if (msg.contains("previous")) processIrreversebleBlock(msg);
            else log.info("unrecogrinzed msg from aunit \n" + msg);
        } catch (Exception e) {
            log.error("Web socket error AUNIT : \n" + e.getMessage());
        }
    }

    @SneakyThrows
    private void getBlock(int blockNum) {
        JSONObject block = new JSONObject();
        block.put("id", 10);
        block.put("method", "call");
        block.put("params", new JSONArray().put(2).put("get_block").put(new JSONArray().put(blockNum)));
        endpoint.sendText(block.toString());
    }

    private void processIrreversebleBlock(String trx) {
        JSONObject block = new JSONObject(trx);
        if (block.getJSONObject("result").getJSONArray("transactions").length() == 0) return;
        JSONArray transactions = block.getJSONObject("result").getJSONArray("transactions");

        List<String> lisfOfMemo = refillService.getListOfValidAddressByMerchantIdAndCurrency(merchant.getId(), currency.getId());
        try {
            for (int i = 0; i < transactions.length(); i++) {
                JSONObject transaction = transactions.getJSONObject(i).getJSONArray("operations").getJSONArray(0).getJSONObject(1);

                if (transaction.getString("to").equals(ccp.getOtherCoins().getAunit().getMainAddress())) {
                    makeRefill(lisfOfMemo, transaction);
                }
            }
        } catch (JSONException e) {
            log.debug(e);
        }
    }

    @SneakyThrows
    private void makeRefill(List<String> lisfOfMemo, JSONObject transaction) {
        JSONObject memo = transaction.getJSONObject("memo");
        try {
            String memoText = decryptBTSmemo(privateKey, memo.toString());
            if (lisfOfMemo.contains(memoText)) {
                BigDecimal amount = reduceAmount(transaction.getJSONObject("amount").getInt("amount"));

                prepareAndProcessTx(Hashing.sha256()
                        .hashString(memo.toString(), StandardCharsets.UTF_8)
                        .toString(), memoText, amount);
            }
        } catch (NoSuchAlgorithmException e) {
            log.error("Memo can not be decrypted : " + e.getClass());
        }
    }

    private void prepareAndProcessTx(String hash, String address, BigDecimal amount) {
        Map<String, String> map = new HashMap<>();
        map.put("address", address);
        map.put("hash", hash);
        map.put("amount", amount.toString());
        aunitService.createRequest(hash, address, amount);
        try {
            aunitService.processPayment(map);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error(e);
        }
    }

    private BigDecimal reduceAmount(int amount) {
        return new BigDecimal(amount).multiply(new BigDecimal(Math.pow(10, -5))).setScale(5, RoundingMode.HALF_DOWN);
    }

    private void setIrreversableBlock(String msg) {
        JSONObject message = new JSONObject(msg);
        int blockNumber = message.getJSONArray("params").getJSONArray(1).getJSONArray(0).getJSONObject(0).getInt("last_irreversible_block_num");
        synchronized (this) {
            if (blockNumber > latIrreversableBlocknumber) {
                for (; latIrreversableBlocknumber <= blockNumber; latIrreversableBlocknumber++) {
                    getBlock(latIrreversableBlocknumber);
                }
                merchantSpecParamsDao.updateParam(merchant.getName(), lastIrreversebleBlock, String.valueOf(latIrreversableBlocknumber));
            }
        }
    }

    @PreDestroy
    public void onShutdown() {
        try {
            session.close();
            log.info("AUNIT web socket closed");
        } catch (IOException e) {
            e.printStackTrace();
            log.error("error closing session");
        }
    }
}


package com.exrates.inout.service.eos;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.EosDataDto;
import com.exrates.inout.domain.dto.MerchantSpecParamDto;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.EosProperty;
import io.jafka.jeos.EosApi;
import io.jafka.jeos.EosApiFactory;
import io.jafka.jeos.core.response.chain.Block;
import io.jafka.jeos.core.response.history.transaction.Transaction;
import lombok.extern.log4j.Log4j2;
import org.apache.log4j.BasicConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Log4j2(topic = "eos_log")
@Service
public class EosReceiveService {

    private EosApi client;
    private static final String LAST_BLOCK_PARAM = "LastScannedBlock";

    private static final String MERCHANT_NAME = "EOS";
    private static final String CURRENCY_NAME = "EOS";

    private static final String TRANSFER = "transfer";
    private static final String EOSIO_ACCOUNT = "eosio.token";
    private static final String EXECUTED = "executed";
    private static final int CONFIRMATIONS_NEEDED = 500;

    private EosProperty eosProperty;

    @Autowired
    private MerchantSpecParamsDao specParamsDao;
    @Autowired
    private EosService eosService;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    private void init() {
        BasicConfigurator.configure();
        client = EosApiFactory.create("http://127.0.0.1:8900",
                "https://api.eosnewyork.io",
                "https://api.eosnewyork.io");
        scheduler.scheduleAtFixedRate(this::checkRefills, 5, 5, TimeUnit.MINUTES);
    }

    @Autowired
    public EosReceiveService(CryptoCurrencyProperties ccp){
        this.eosProperty = ccp.getEosCoins().getEos();
    }

    private void checkRefills() {
        long lastBlock = loadLastBlock();
        long blockchainHeight = getLastBlockNum();
        while (lastBlock < blockchainHeight - CONFIRMATIONS_NEEDED) {
            Block block = client.getBlock(String.valueOf(++lastBlock));
            List<Transaction> transactionList = Arrays.asList(block.getTransactions());
            transactionList.forEach(transaction -> {
                if (transaction.getStatus().equals(EXECUTED)) {
                    transaction.getTrx().ifPresent(trx -> {
                        List<io.jafka.jeos.core.common.Action> actions = trx.getTransaction().getActions();
                        actions.forEach(action -> {
                            String operation = action.getName();
                            if (operation.equalsIgnoreCase(TRANSFER) && action.getAccount().equals(EOSIO_ACCOUNT)) {
                                EosDataDto dataDto = new EosDataDto((LinkedHashMap) action.getData());
                                if (dataDto.getToAccount().equals(eosProperty.getEosMainAddress()) && dataDto.getCurrency().equals(CURRENCY_NAME)) {
                                    processTransaction(dataDto, trx.getId());
                                }
                            }
                        });
                    });
                }
            });
            saveLastBlock(lastBlock);
        }
    }

    private long getLastBlockNum() {
        return client.getChainInfo().getLastIrreversibleBlockNum();
    }

    private void processTransaction(EosDataDto dataDto, String hash) {
        Map<String, String> map = new HashMap<>();
        map.put("address", dataDto.getMemo());
        map.put("hash", hash);
        map.put("amount", dataDto.getAmount().toPlainString());

        eosService.processPayment(map);
    }

    private void saveLastBlock(long blockNum) {
        specParamsDao.updateParam(MERCHANT_NAME, LAST_BLOCK_PARAM, String.valueOf(blockNum));
    }

    private Long loadLastBlock() {
        MerchantSpecParamDto specParamsDto = specParamsDao.getByMerchantNameAndParamName(MERCHANT_NAME, LAST_BLOCK_PARAM);
        return specParamsDto == null ? 0 : Long.valueOf(specParamsDto.getParamValue());
    }
}

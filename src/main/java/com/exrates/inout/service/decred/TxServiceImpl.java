package com.exrates.inout.service.decred;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.MerchantSpecParamDto;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.decred.grpc.DecredApi;
import com.google.protobuf.ByteString;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2(topic = "decred")
@Service
public class TxServiceImpl implements TxService {

    @Autowired
    private MerchantSpecParamsDao specParamsDao;
    @Autowired
    private DecredService decredService;
    @Autowired
    private DecredGrpcService decredGrpcService;

    private static final String LAST_HASH_PARAM = "LastBlock";
    private static final String MERCHANT_NAME = "DCR";
    private static final Integer BLOCKS_OFFSET_TO_SCAN = 4;

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @PostConstruct
    private void init() {
        scheduler.scheduleAtFixedRate(this::checkTransactions, 180, 300, TimeUnit.SECONDS);
    }


    @Override
    public void checkTransactions() {
        try {
            log.debug("decred check txs");
            int lastBlockToScan = decredGrpcService.getBlockInfo().getHeight() - BLOCKS_OFFSET_TO_SCAN;
            int firstBlockToScan = Integer.valueOf(loadLastBlock()) + 1;
            log.debug("first block {}, last block {}", firstBlockToScan, lastBlockToScan);
            if (firstBlockToScan >= lastBlockToScan) {
                return;
            }
            Iterator<DecredApi.GetTransactionsResponse> response = decredGrpcService.getTransactions(firstBlockToScan, lastBlockToScan);
            log.debug("response has next {}",response.hasNext());
            while (response.hasNext())
            {
                DecredApi.GetTransactionsResponse txResp = response.next();
                log.debug("tx response {}", txResp.hasMinedTransactions());
                List<DecredApi.TransactionDetails> transactionDetails = txResp.getMinedTransactions().getTransactionsList();
                DecredApi.BlockDetails blockDetails = txResp.getMinedTransactions();
                Integer block = blockDetails.getHeight();
                saveLastHash(block.toString());
                transactionDetails.forEach(tr-> {
                    log.debug("income tx {}", tr);
                    DecredApi.TransactionDetails.TransactionType transactionType = tr.getTransactionType();
                   if (transactionType.equals(DecredApi.TransactionDetails.TransactionType.REGULAR)) {
                       ByteString hash = tr.getHash();
                       tr.getCreditsList().forEach(dl -> {
                           String address = dl.getAddress();
                           long amount = dl.getAmount();
                           if (decredService.getAddresses().contains(address)) {
                               tryToRefill(address, hash.toByteArray(), amount);
                           }
                       });
                   }
                });
            }
        } catch (NumberFormatException e) {
            log.error(e);
        }
    }

    private void tryToRefill(String address, byte[] hash, long amount) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("address", address);
            map.put("hash", decodeHash(hash));
            map.put("amount", normalizeAmount(amount));
            decredService.processPayment(map);
        } catch (RefillRequestAppropriateNotFoundException e) {
            log.error(e);
        }
    }

    private String decodeHash(byte[] bytes) {
        if (bytes.length % 2 != 0) {
            bytes =  org.apache.commons.lang3.ArrayUtils.insert(0, bytes, (byte) 0);
        }
        ArrayUtils.reverse(bytes);
        return String.copyValueOf(Hex.encode(bytes));
    }

    private String normalizeAmount(long amount) {
        BigDecimal bdAmount = BigDecimal.valueOf(amount).divide(new BigDecimal(100000000), 8, RoundingMode.HALF_UP);
        return bdAmount.toString();
    }

    private void saveLastHash(String blockId) {
        specParamsDao.updateParam(MERCHANT_NAME, LAST_HASH_PARAM, blockId);
    }


    private String loadLastBlock() {
        MerchantSpecParamDto specParamsDto = specParamsDao.getByMerchantNameAndParamName(MERCHANT_NAME, LAST_HASH_PARAM);
        return specParamsDto == null ? "0" : specParamsDto.getParamValue();
    }
}

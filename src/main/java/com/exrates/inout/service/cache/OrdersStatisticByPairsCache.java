package com.exrates.inout.service.cache;

import com.exrates.inout.dao.CurrencyDao;
import com.exrates.inout.dao.OrderDao;
import com.exrates.inout.domain.dto.ExOrderStatisticsShortByPairsDto;
import com.exrates.inout.domain.main.CurrencyPair;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2(topic = "cache")
@Component
public class OrdersStatisticByPairsCache {

    @Autowired
    private OrderDao orderDao;

    private List<ExOrderStatisticsShortByPairsDto> allStatsCachedList = new CopyOnWriteArrayList<>();

    private List<ExOrderStatisticsShortByPairsDto> cachedList = new CopyOnWriteArrayList<>();

    private Semaphore semaphore = new Semaphore(1, true);

    private CyclicBarrier barrier = new CyclicBarrier(1000);

    private AtomicBoolean needUpdate = new AtomicBoolean(true);

   /* @PostConstruct
    private void init() {
        allPairs = currencyDao.getAllCurrencyPairs(CurrencyPairType.ALL);
        update();
        needUpdate.set(false);
        log.info("initialized, {}", cachedList.size());
    }*/




    public void update() {
        if (semaphore.tryAcquire()) {
            updateCache();
        }
    }

    private void updateCache() {
        try {
            log.info("try update cache");
            log.info("update cache");
            this.cachedList = orderDao.getOrderStatisticByPairs();
            needUpdate.set(false);
        } finally {
            semaphore.release();
        }
    }

}

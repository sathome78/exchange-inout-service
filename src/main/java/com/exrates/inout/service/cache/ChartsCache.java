package com.exrates.inout.service.cache;

import lombok.extern.log4j.Log4j2;
import me.exrates.model.CurrencyPair;
import me.exrates.model.enums.CurrencyPairType;
import me.exrates.service.CurrencyService;
import me.exrates.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Maks on 29.12.2017.
 */

@PropertySource("classpath:cache.properties")
@Log4j2(topic = "cache")
@Component
public class ChartsCache {

    private @Value("${pairs_lazy_load}")boolean lazyLoad;

    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private OrderService orderService;

    /**Map <pairId, <interval, data>>
     * */
    private Map<Integer, Map<String, String>> cacheMap = new ConcurrentHashMap<>();
    private Map<Integer, Semaphore> locksMap = new ConcurrentHashMap<>();
    private Map<Integer, ReentrantLock> secondLocksMap = new ConcurrentHashMap<>();
    private Map<Integer, CountDownLatch> countDownLocksMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        if (!lazyLoad) {
            List<CurrencyPair> pairs = currencyService.getAllCurrencyPairs(CurrencyPairType.ALL);
            pairs.forEach(p -> {
                log.debug("start initialize cache for {}", p.getName());
                updateCache(p.getId());
                log.debug("cache for {} initialized!", p.getName());
            });
        }
    }

    public String getDataForPeriod(Integer pairId, String interval) {
        return getData(pairId).get(interval);
    }


    public Map<String, String> getData(Integer currencyPairId) {
        if (!cacheMap.containsKey(currencyPairId)) {
            updateCache(currencyPairId);
        }
        return cacheMap.get(currencyPairId);
    }

   /*todo: update only subscribed intervals
   public Map<String, String> getData(Integer currencyPairId, List<BackDealInterval> intervals) {

        log.debug("get data for pair {}", currencyPairId);
        if (!cacheMap.containsKey(currencyPairId)) {
            log.debug("no key {}", currencyPairId );
            updateCache(currencyPairId);
        }
        return cacheMap.get(currencyPairId);
    }*/

    public void updateCache(Integer currencyPairId) {
        Semaphore currentSemaphore = locksMap.computeIfAbsent(currencyPairId, p -> new Semaphore(1));
        ReentrantLock currentLock = secondLocksMap.computeIfAbsent(currencyPairId, p -> new ReentrantLock(true));
        CountDownLatch currentCountDownLock = countDownLocksMap.computeIfAbsent(currencyPairId, p -> new CountDownLatch(1));
        if (currentSemaphore.tryAcquire()) {
            currentLock.lock();
            Map<String, String> map = cacheMap.computeIfAbsent(currencyPairId,
                    p -> new ConcurrentHashMap<>());
            orderService.getIntervals().forEach(p -> {
                map.put(p.getInterval(), orderService.getChartData(currencyPairId, p));
            });
            currentSemaphore.release();
            currentCountDownLock.countDown();
            currentLock.unlock();
        } else if(currentLock.isLocked()) {
            try {
                currentCountDownLock.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }

}

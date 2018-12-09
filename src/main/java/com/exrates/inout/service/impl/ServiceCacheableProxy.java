package com.exrates.inout.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Log4j2
@Transactional(propagation = Propagation.MANDATORY)
public class ServiceCacheableProxy {

//  @Autowired
//  private OrderDao orderDao;
//
//  @CacheEvict(cacheNames = "orderBuy", key = "#currencyPair.id", condition = "#evictCache", beforeInvocation = true)
//  @Cacheable(cacheNames = "orderBuy", key = "#currencyPair.id")
//  public List<OrderListDto> getAllBuyOrders(
//          CurrencyPair currencyPair,
//          UserRole filterRole, Boolean evictCache) {
//    log.debug(String.format("\n%s evictCache: %s", currencyPair, evictCache));
//    return orderDao.getOrdersBuyForCurrencyPair(currencyPair, filterRole);
//  }
//
//  @CacheEvict(cacheNames = "orderSell", key = "#currencyPair.id", condition = "#evictCache", beforeInvocation = true)
//  @Cacheable(cacheNames = "orderSell", key = "#currencyPair.id")
//  public List<OrderListDto> getAllSellOrders(
//          CurrencyPair currencyPair,
//          UserRole filterRole, Boolean evictCache) {
//    log.debug(String.format("\n%s evictCache: %s", currencyPair, evictCache));
//    return orderDao.getOrdersSellForCurrencyPair(currencyPair, filterRole);
//  }
}

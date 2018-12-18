package com.exrates.inout.dao;

import java.util.List;

public interface EthereumNodeDao {

    List<String> findAllAddresses(String merchant);
}

package com.exrates.inout.service.impl;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import me.exrates.service.NodeCheckerService;
import com.exrates.inout.service.IRefillable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class NodeCheckerServiceImpl implements NodeCheckerService {

    private final Map<String, IRefillable> merchantNodeMap = new HashMap<>();;

    public NodeCheckerServiceImpl(Map<String, IRefillable> bitcoinServiceMap) {
        for (Map.Entry<String, IRefillable> entry : bitcoinServiceMap.entrySet()) {
            merchantNodeMap.put(entry.getValue().getMerchantName(), entry.getValue());
        }
    }

    @Override
    public Long getBTCBlocksCount(String ticker) {
        try {
            return merchantNodeMap.get(ticker).getBlocksCount();
        } catch (Exception e){
            return null;
        }
    }

    @Override
    public List<String> listOfRefillableServicesNames() {
        return new LinkedList<>(merchantNodeMap.keySet());
    }

    @Override
    public Long getLastBlockTime(String ticker) throws BitcoindException, CommunicationException {
        return  merchantNodeMap.get(ticker).getLastBlockTime();
    }
}
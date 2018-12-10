package com.exrates.inout.service.achain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class AchainTokenContext {

    private final Map<String, AchainContract> contractsMap;

    private Map<String, AchainContract> contractIdMap = new HashMap<>();

    @Autowired
    public AchainTokenContext(Map<String, AchainContract> contractsMap) {
        this.contractsMap = contractsMap;
    }

    @PostConstruct
    private void init() {
        contractsMap.forEach((k,v)-> {
            contractIdMap.put(v.getContract(), v);
        });
    }

    AchainContract getByContractId(String contractId) {
        return contractIdMap.get(contractId);
    }
}

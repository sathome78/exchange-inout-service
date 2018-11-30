package com.exrates.inout.service.btc;

import org.springframework.stereotype.Service;

@Service("SICBlockChecker")
public class SICBlockChecker implements BitcoinBlocksCheckerService {
    @Override
    public long getExplorerBlocksAmount() {
        return 0;
    }
}

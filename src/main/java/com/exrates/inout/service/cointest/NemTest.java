package com.exrates.inout.service.cointest;

import com.exrates.inout.service.nem.NemNodeService;
import com.exrates.inout.service.nem.NemRecieveTransactionsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

public class NemTest extends CoinTestBasic {

    @Autowired
    private NemNodeService nodeService;
    @Autowired
    private NemRecieveTransactionsService transactionsService;

    protected NemTest(String name, String email, StringBuilder stringBuilder) {
        super(name, email, stringBuilder);
    }

    @Override
    @SneakyThrows
    public void testCoin(String refillAmount) {
        checkGetIncomeTrxMethod();

        long lastBlockHeight = nodeService.getLastBlockHeight();
        stringBuilder.append("Last block from node: ").append(lastBlockHeight).append("\n");
        stringBuilder.append("Starting checking node syncing...");

        checkNodeSyncing(lastBlockHeight);

        stringBuilder.append("Works fine!").append("\n");

    }

    private void checkGetIncomeTrxMethod() {
        stringBuilder.append("Starting testing getIncomeTransactions method...").append("\n");
        nodeService.getIncomeTransactions(ccp.getNemCoins().getNem().getAddress(), transactionsService.loadLastHash());
        stringBuilder.append("Done").append("\n");
    }

    private void checkNodeSyncing(long lastBlockHeight) throws InterruptedException {
        long currentBlockHeight;
        do {
            currentBlockHeight = nodeService.getLastBlockHeight();
            stringBuilder.append("Current height = ").append(nodeService.getLastBlockHeight()).append("\n");
            Thread.sleep(30*1000);
        } while (currentBlockHeight <= lastBlockHeight);
    }
}

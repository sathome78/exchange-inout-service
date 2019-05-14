package com.exrates.inout.service.cointest;

import com.exrates.inout.service.nem.NemNodeService;
import com.exrates.inout.service.nem.NemRecieveTransactionsService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("nemTokenTest")
@Scope("prototype")
public class NemTokenTest extends CoinTestBasic{

    @Autowired
    private NemNodeService nodeService;
    @Autowired
    private NemRecieveTransactionsService transactionsService;

    protected NemTokenTest(String name, String email, StringBuilder stringBuilder) {
        super(name, email, stringBuilder);
    }

    @Override
    @SneakyThrows
    public void testCoin(String refillAmount) {
        stringBuilder.append("Starting testing getIncomeTransactions method...").append("\n");
        nodeService.getIncomeTransactions(ccp.getNemCoins().getNem().getAddress(), transactionsService.loadLastHash());
        stringBuilder.append("Done").append("\n");
        long lastBlockHeight = nodeService.getLastBlockHeight();
        stringBuilder.append("Last block from node: ").append(lastBlockHeight).append("\n");
        stringBuilder.append("Starting checking node syncing...");

        long currentBlockHeight;

        do {
            currentBlockHeight = nodeService.getLastBlockHeight();
            stringBuilder.append("Current height = " + nodeService.getLastBlockHeight()).append("\n");
            Thread.sleep(30*1000);
        } while (currentBlockHeight <= lastBlockHeight);

        stringBuilder.append("Works fine!").append("\n");

    }
}

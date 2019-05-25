package com.exrates.inout.service.cointest;

import com.exrates.inout.service.waves.WavesRestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;

public class WavesTest extends CoinTestBasic{

    @Autowired
    private WavesRestClient wavesRestClient;

    protected WavesTest(String name, String email, StringBuilder stringBuilder) {
        super(name, email, stringBuilder);
    }

    @Override
    @SneakyThrows
    public void testCoin(String refillAmount) {
        long lastBlockHeight = wavesRestClient.getCurrentBlockHeight();
        stringBuilder.append("Last block from node: ").append(lastBlockHeight).append("\n");
        stringBuilder.append("Starting checking node syncing...");

        checkNodeSyncing(lastBlockHeight);

        stringBuilder.append("Works fine!").append("\n");

    }

    private void checkNodeSyncing(long lastBlockHeight) throws InterruptedException {
        long currentBlockHeight;
        do {
            currentBlockHeight = wavesRestClient.getCurrentBlockHeight();
            stringBuilder.append("Current height = ").append(wavesRestClient.getCurrentBlockHeight()).append("\n");
            Thread.sleep(30*1000);
        } while (currentBlockHeight <= lastBlockHeight);
    }
}

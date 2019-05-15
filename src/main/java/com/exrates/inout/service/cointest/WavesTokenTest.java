package com.exrates.inout.service.cointest;

import com.exrates.inout.service.waves.WavesRestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("nemTokenTest")
@Scope("prototype")
public class WavesTokenTest extends CoinTestBasic{

    @Autowired
    private WavesRestClient wavesRestClient;

    protected WavesTokenTest(String name, String email, StringBuilder stringBuilder) {
        super(name, email, stringBuilder);
    }

    @Override
    @SneakyThrows
    public void testCoin(String refillAmount) {
        long lastBlockHeight = wavesRestClient.getCurrentBlockHeight();
        stringBuilder.append("Last block from node: ").append(lastBlockHeight).append("\n");
        stringBuilder.append("Starting checking node syncing...");

        long currentBlockHeight;

        do {
            currentBlockHeight = wavesRestClient.getCurrentBlockHeight();
            stringBuilder.append("Current height = " + wavesRestClient.getCurrentBlockHeight()).append("\n");
            Thread.sleep(30*1000);
        } while (currentBlockHeight <= lastBlockHeight);

        stringBuilder.append("Works fine!").append("\n");

    }
}

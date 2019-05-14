package com.exrates.inout.controller;

import com.exrates.inout.service.CoinTester;
import com.exrates.inout.service.cointest.CoinDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
public class CoinTestController {

    private final CoinDispatcher coinDispatcher;

    private StringBuilder logger = new StringBuilder("to be continued...");
    private ExecutorService executor =  Executors.newCachedThreadPool();
    private Thread activeThread = new Thread();

    @Autowired
    public CoinTestController(CoinDispatcher coinDispatcher) {
        this.coinDispatcher = coinDispatcher;
    }

    @GetMapping("/cointest/start")
    public String startTesting(@RequestParam(name = "coin") String name, @RequestParam(name = "amount") String amount,
                               @RequestParam(name = "email", required = false) String email) throws Exception {
        logger = new StringBuilder();
        logger.append("<a href = \"/cointest/stop\">STOP</a>").append("<br>");
        activeThread = new Thread(() -> startCoinTest(name, amount, email));
        activeThread.start();
        return "redirect:/cointest/log";
    }

    private void startCoinTest(@RequestParam(name = "coin") String name, @RequestParam(name = "amount") String amount, @RequestParam(name = "email", required = false) String email) {
        try {
            CoinTester coinTester = coinDispatcher.getCoinTester(name, logger, email);
            coinTester.testCoin(amount);
        } catch (Exception e){
            logger.append(e.getMessage()).append("\n");
        }
    }

    @GetMapping(value = "/cointest/log", produces = "text/html")
    @ResponseBody //TODO make updatable
    public String getLog(){
        return logger.toString();
    }

    @GetMapping(value = "/cointest/stop", produces = "text/plain")
    @ResponseBody
    public String stop(){
        activeThread.stop();
        return "stopped";
    }

}

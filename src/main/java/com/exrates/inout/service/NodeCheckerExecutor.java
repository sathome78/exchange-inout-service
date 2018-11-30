package com.exrates.inout.service;

import com.exrates.inout.exceptions.NoSynchronizedException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class NodeCheckerExecutor {

    private Map<String, NodeChecker> nodes;

    @Scheduled
    public void check(){
        ExecutorService executor = Executors.newFixedThreadPool(nodes.size());

        nodes.forEach((name, service) -> {
            checkIfAlive(service);
            checkSynchronizing(service);
        });
    }

    private void checkSynchronizing(NodeChecker service) {
       try {
           if(!service.isSynchronized()){
               throw new NoSynchronizedException();
           }
       } catch (Exception e){
            //todo send email/telegram with info
       }
    }

    private void checkIfAlive(NodeChecker service) {
        try {
            service.isAlive();
        } catch (Exception e) {
            e.printStackTrace();
            //todo send email/telegram with info
        }
    }

}

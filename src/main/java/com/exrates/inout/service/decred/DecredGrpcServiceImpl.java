package com.exrates.inout.service.decred;

import com.exrates.inout.service.decred.grpc.DecredApi;
import com.exrates.inout.service.decred.grpc.WalletServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Iterator;

@Service
@Log4j2(topic = "decred")
public class DecredGrpcServiceImpl implements DecredGrpcService {

    @Value("${decred.node.host}")
    private String host;
    @Value("${decred.node.port}")
    private String port;
    @Value("${decred.node.cert-path}")
    private String path;

    private ManagedChannel channel = null;

    @PostConstruct
    private void init() {
        try {
            connect();
        } catch (Exception e) {
            log.error("error connect to dcrwallet");
        }
    }

    private ManagedChannel getChannel() {
        checkConnect();
        return channel;
    }

    private void connect() {
        log.debug("connect");
        try {
            File initialFile = new File(path);
            InputStream streamFirst = new FileInputStream(initialFile);
            log.debug("stream size {}", streamFirst.available());
            String cert = IOUtils.toString(streamFirst, "UTF-8");
            log.debug(cert);
            InputStream stream = new FileInputStream(initialFile);
            log.debug("stream size {}", stream.available());
            channel = NettyChannelBuilder.forAddress(host, Integer.valueOf(port))
                    .sslContext(GrpcSslContexts
                            .forClient()
                            .trustManager(stream)
                            .build())
                    .build();
        } catch (Exception e) {
            log.error(e);
            /*throw new RuntimeException(e);*/
        }
        log.debug("channel created");
    }

    private void checkConnect() {
        if (channel == null || channel.isShutdown()) {
            connect();
        }
    }

    @Override
    public DecredApi.NextAddressResponse getNewAddress() {
        WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(getChannel());
        return stub.nextAddress(DecredApi.NextAddressRequest
                .newBuilder()
                .setKind(DecredApi.NextAddressRequest.Kind.BIP0044_INTERNAL)
                .setAccount(0)
                .setGapPolicy(DecredApi.NextAddressRequest.GapPolicy.GAP_POLICY_IGNORE)
                .build());
    }

    @Override
    public Iterator<DecredApi.GetTransactionsResponse> getTransactions(int startBlock, int endBlockHeight) {
        WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(getChannel());
        return stub.getTransactions(DecredApi.GetTransactionsRequest
                .newBuilder()
                .setStartingBlockHeight(startBlock)
                .setEndingBlockHeight(endBlockHeight)
                .build());
    }

    @Override
    public DecredApi.BestBlockResponse getBlockInfo() {
        WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(getChannel());
        return stub.bestBlock(DecredApi.BestBlockRequest.getDefaultInstance());
    }

    @PreDestroy
    private void destroy() {
        channel.shutdown();
    }
}

package com.exrates.inout.service.decred;


import com.exrates.inout.service.decred.grpc.DecredApi;
import com.exrates.inout.service.decred.grpc.WalletServiceGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyChannelBuilder;


import java.io.InputStream;
import java.util.Iterator;
import java.util.List;


public class TestGrpcService {

        private static ManagedChannel channel = null;

        public static void main(String[] args) {
            try {
                ClassLoader loader = TestGrpcService.class.getClassLoader();
                InputStream streamCert = loader.getResourceAsStream("ca.crt");
                channel = NettyChannelBuilder.forAddress("172.31.0.148", 9111)
                        .sslContext(GrpcSslContexts
                                .forClient()
                                .trustManager(streamCert)
                                .build())
                        .build();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }
            getTransactions();
            channel.shutdown();
        }

     private static void newAddress() {
        WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(channel);
        DecredApi.NextAddressResponse response = stub.nextAddress(DecredApi.NextAddressRequest
                .newBuilder()
                .setKind(DecredApi.NextAddressRequest.Kind.BIP0044_INTERNAL)
                .setAccount(0)
                .setGapPolicy(DecredApi.NextAddressRequest.GapPolicy.GAP_POLICY_IGNORE)
                .build());
        System.out.println("address " + response.getAddress() + " pk " + response.getPublicKey());
    }

        private static void getBalance() {
            WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(channel);
            DecredApi.BalanceResponse response = stub.balance(DecredApi.BalanceRequest.getDefaultInstance());
            System.out.println(response);
            System.out.println("spendable " + response.getSpendable() + " total " + response.getTotal() + " unconfirmed " + response.getUnconfirmed());
        }

    private static void getTransaction() {
            String hash = "16a7882d9e4019c9e7ed88100ce10c6629c21d804d3a6de733593dc7eaed91b7";
            ByteString bytes = ByteString.copyFrom(hash.getBytes());
            WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(channel);
            DecredApi.GetTransactionResponse response = stub.getTransaction(DecredApi.GetTransactionRequest.newBuilder().setTransactionHash(bytes).build());
            System.out.println(response.toString());
    }

    private static void getTransactions() {
            WalletServiceGrpc.WalletServiceBlockingStub stub = WalletServiceGrpc.newBlockingStub(channel);
            Iterator<DecredApi.GetTransactionsResponse> response = stub.getTransactions(DecredApi.GetTransactionsRequest
                    .newBuilder()
                    .setStartingBlockHeight(255561)
                    .build());

            while (response.hasNext())
            {
                DecredApi.GetTransactionsResponse txResp = response.next();
                List<DecredApi.TransactionDetails> transactionDetails = txResp.getMinedTransactions().getTransactionsList();
                DecredApi.BlockDetails blockDetails = txResp.getMinedTransactions();
                int block = blockDetails.getHeight();
                System.out.println("block hash "  + blockDetails.getHash().toStringUtf8());

                transactionDetails.forEach(tr-> {
                    DecredApi.TransactionDetails.TransactionType transactionType = tr.getTransactionType();
                    System.out.println("tx type " + transactionType.name());
                    tr.getCreditsList().forEach(dl-> {
                        System.out.println(dl);
                        String address = dl.getAddress();
                        long amount = dl.getAmount();
                    });
                    ByteString hash = tr.getHash();
                    decodeHash(hash.toByteArray());
                    System.out.println("-__________________________-");
                });
            }
        }

        private static void decodeHash(byte[] bytes) {
            if (bytes.length % 2 != 0) {
                bytes =  org.apache.commons.lang3.ArrayUtils.insert(0, bytes, (byte) 0);
            }
            org.apache.commons.lang3.ArrayUtils.reverse(bytes);
            System.out.print("hash ");
            System.out.println(String.copyValueOf(org.springframework.security.crypto.codec.Hex.encode(bytes)));
        }
}

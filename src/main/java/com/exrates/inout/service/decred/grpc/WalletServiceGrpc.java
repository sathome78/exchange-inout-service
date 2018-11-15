package com.exrates.inout.service.decred.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.10.1)",
    comments = "Source: decred_api.proto")
public final class WalletServiceGrpc {

  private WalletServiceGrpc() {}

  public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.WalletService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPingMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> METHOD_PING = getPingMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> getPingMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> getPingMethod() {
    return getPingMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> getPingMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest, com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> getPingMethod;
    if ((getPingMethod = WalletServiceGrpc.getPingMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getPingMethod = WalletServiceGrpc.getPingMethod) == null) {
          WalletServiceGrpc.getPingMethod = getPingMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.PingRequest, com.exrates.inout.service.decred.grpc.DecredApi.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "Ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PingResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("Ping"))
                  .build();
          }
        }
     }
     return getPingMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getNetworkMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> METHOD_NETWORK = getNetworkMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> getNetworkMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> getNetworkMethod() {
    return getNetworkMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> getNetworkMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest, com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> getNetworkMethod;
    if ((getNetworkMethod = WalletServiceGrpc.getNetworkMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getNetworkMethod = WalletServiceGrpc.getNetworkMethod) == null) {
          WalletServiceGrpc.getNetworkMethod = getNetworkMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest, com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "Network"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("Network"))
                  .build();
          }
        }
     }
     return getNetworkMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAccountNumberMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> METHOD_ACCOUNT_NUMBER = getAccountNumberMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> getAccountNumberMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> getAccountNumberMethod() {
    return getAccountNumberMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> getAccountNumberMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> getAccountNumberMethod;
    if ((getAccountNumberMethod = WalletServiceGrpc.getAccountNumberMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getAccountNumberMethod = WalletServiceGrpc.getAccountNumberMethod) == null) {
          WalletServiceGrpc.getAccountNumberMethod = getAccountNumberMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "AccountNumber"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("AccountNumber"))
                  .build();
          }
        }
     }
     return getAccountNumberMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAccountsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> METHOD_ACCOUNTS = getAccountsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> getAccountsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> getAccountsMethod() {
    return getAccountsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> getAccountsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> getAccountsMethod;
    if ((getAccountsMethod = WalletServiceGrpc.getAccountsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getAccountsMethod = WalletServiceGrpc.getAccountsMethod) == null) {
          WalletServiceGrpc.getAccountsMethod = getAccountsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "Accounts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("Accounts"))
                  .build();
          }
        }
     }
     return getAccountsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getBalanceMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> METHOD_BALANCE = getBalanceMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> getBalanceMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> getBalanceMethod() {
    return getBalanceMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> getBalanceMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest, com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> getBalanceMethod;
    if ((getBalanceMethod = WalletServiceGrpc.getBalanceMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getBalanceMethod = WalletServiceGrpc.getBalanceMethod) == null) {
          WalletServiceGrpc.getBalanceMethod = getBalanceMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest, com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "Balance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("Balance"))
                  .build();
          }
        }
     }
     return getBalanceMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetAccountExtendedPubKeyMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> METHOD_GET_ACCOUNT_EXTENDED_PUB_KEY = getGetAccountExtendedPubKeyMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> getGetAccountExtendedPubKeyMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> getGetAccountExtendedPubKeyMethod() {
    return getGetAccountExtendedPubKeyMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> getGetAccountExtendedPubKeyMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> getGetAccountExtendedPubKeyMethod;
    if ((getGetAccountExtendedPubKeyMethod = WalletServiceGrpc.getGetAccountExtendedPubKeyMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getGetAccountExtendedPubKeyMethod = WalletServiceGrpc.getGetAccountExtendedPubKeyMethod) == null) {
          WalletServiceGrpc.getGetAccountExtendedPubKeyMethod = getGetAccountExtendedPubKeyMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "GetAccountExtendedPubKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("GetAccountExtendedPubKey"))
                  .build();
          }
        }
     }
     return getGetAccountExtendedPubKeyMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetTransactionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> METHOD_GET_TRANSACTION = getGetTransactionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> getGetTransactionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> getGetTransactionMethod() {
    return getGetTransactionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> getGetTransactionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> getGetTransactionMethod;
    if ((getGetTransactionMethod = WalletServiceGrpc.getGetTransactionMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getGetTransactionMethod = WalletServiceGrpc.getGetTransactionMethod) == null) {
          WalletServiceGrpc.getGetTransactionMethod = getGetTransactionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "GetTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("GetTransaction"))
                  .build();
          }
        }
     }
     return getGetTransactionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetTransactionsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> METHOD_GET_TRANSACTIONS = getGetTransactionsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> getGetTransactionsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> getGetTransactionsMethod() {
    return getGetTransactionsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> getGetTransactionsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> getGetTransactionsMethod;
    if ((getGetTransactionsMethod = WalletServiceGrpc.getGetTransactionsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getGetTransactionsMethod = WalletServiceGrpc.getGetTransactionsMethod) == null) {
          WalletServiceGrpc.getGetTransactionsMethod = getGetTransactionsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "GetTransactions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("GetTransactions"))
                  .build();
          }
        }
     }
     return getGetTransactionsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetTicketMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> METHOD_GET_TICKET = getGetTicketMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketMethod() {
    return getGetTicketMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketMethod;
    if ((getGetTicketMethod = WalletServiceGrpc.getGetTicketMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getGetTicketMethod = WalletServiceGrpc.getGetTicketMethod) == null) {
          WalletServiceGrpc.getGetTicketMethod = getGetTicketMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "GetTicket"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("GetTicket"))
                  .build();
          }
        }
     }
     return getGetTicketMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getGetTicketsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> METHOD_GET_TICKETS = getGetTicketsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketsMethod() {
    return getGetTicketsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getGetTicketsMethod;
    if ((getGetTicketsMethod = WalletServiceGrpc.getGetTicketsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getGetTicketsMethod = WalletServiceGrpc.getGetTicketsMethod) == null) {
          WalletServiceGrpc.getGetTicketsMethod = getGetTicketsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "GetTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("GetTickets"))
                  .build();
          }
        }
     }
     return getGetTicketsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getTicketPriceMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> METHOD_TICKET_PRICE = getTicketPriceMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> getTicketPriceMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> getTicketPriceMethod() {
    return getTicketPriceMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> getTicketPriceMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest, com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> getTicketPriceMethod;
    if ((getTicketPriceMethod = WalletServiceGrpc.getTicketPriceMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getTicketPriceMethod = WalletServiceGrpc.getTicketPriceMethod) == null) {
          WalletServiceGrpc.getTicketPriceMethod = getTicketPriceMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest, com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "TicketPrice"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("TicketPrice"))
                  .build();
          }
        }
     }
     return getTicketPriceMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStakeInfoMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> METHOD_STAKE_INFO = getStakeInfoMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> getStakeInfoMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> getStakeInfoMethod() {
    return getStakeInfoMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> getStakeInfoMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest, com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> getStakeInfoMethod;
    if ((getStakeInfoMethod = WalletServiceGrpc.getStakeInfoMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getStakeInfoMethod = WalletServiceGrpc.getStakeInfoMethod) == null) {
          WalletServiceGrpc.getStakeInfoMethod = getStakeInfoMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest, com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "StakeInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("StakeInfo"))
                  .build();
          }
        }
     }
     return getStakeInfoMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getBlockInfoMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> METHOD_BLOCK_INFO = getBlockInfoMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> getBlockInfoMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> getBlockInfoMethod() {
    return getBlockInfoMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> getBlockInfoMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest, com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> getBlockInfoMethod;
    if ((getBlockInfoMethod = WalletServiceGrpc.getBlockInfoMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getBlockInfoMethod = WalletServiceGrpc.getBlockInfoMethod) == null) {
          WalletServiceGrpc.getBlockInfoMethod = getBlockInfoMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest, com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "BlockInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("BlockInfo"))
                  .build();
          }
        }
     }
     return getBlockInfoMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getBestBlockMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> METHOD_BEST_BLOCK = getBestBlockMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> getBestBlockMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> getBestBlockMethod() {
    return getBestBlockMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> getBestBlockMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest, com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> getBestBlockMethod;
    if ((getBestBlockMethod = WalletServiceGrpc.getBestBlockMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getBestBlockMethod = WalletServiceGrpc.getBestBlockMethod) == null) {
          WalletServiceGrpc.getBestBlockMethod = getBestBlockMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest, com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "BestBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("BestBlock"))
                  .build();
          }
        }
     }
     return getBestBlockMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getTransactionNotificationsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> METHOD_TRANSACTION_NOTIFICATIONS = getTransactionNotificationsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> getTransactionNotificationsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> getTransactionNotificationsMethod() {
    return getTransactionNotificationsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> getTransactionNotificationsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> getTransactionNotificationsMethod;
    if ((getTransactionNotificationsMethod = WalletServiceGrpc.getTransactionNotificationsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getTransactionNotificationsMethod = WalletServiceGrpc.getTransactionNotificationsMethod) == null) {
          WalletServiceGrpc.getTransactionNotificationsMethod = getTransactionNotificationsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "TransactionNotifications"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("TransactionNotifications"))
                  .build();
          }
        }
     }
     return getTransactionNotificationsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAccountNotificationsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> METHOD_ACCOUNT_NOTIFICATIONS = getAccountNotificationsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> getAccountNotificationsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> getAccountNotificationsMethod() {
    return getAccountNotificationsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> getAccountNotificationsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> getAccountNotificationsMethod;
    if ((getAccountNotificationsMethod = WalletServiceGrpc.getAccountNotificationsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getAccountNotificationsMethod = WalletServiceGrpc.getAccountNotificationsMethod) == null) {
          WalletServiceGrpc.getAccountNotificationsMethod = getAccountNotificationsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "AccountNotifications"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("AccountNotifications"))
                  .build();
          }
        }
     }
     return getAccountNotificationsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getConfirmationNotificationsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> METHOD_CONFIRMATION_NOTIFICATIONS = getConfirmationNotificationsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> getConfirmationNotificationsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> getConfirmationNotificationsMethod() {
    return getConfirmationNotificationsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> getConfirmationNotificationsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> getConfirmationNotificationsMethod;
    if ((getConfirmationNotificationsMethod = WalletServiceGrpc.getConfirmationNotificationsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getConfirmationNotificationsMethod = WalletServiceGrpc.getConfirmationNotificationsMethod) == null) {
          WalletServiceGrpc.getConfirmationNotificationsMethod = getConfirmationNotificationsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ConfirmationNotifications"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ConfirmationNotifications"))
                  .build();
          }
        }
     }
     return getConfirmationNotificationsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getChangePassphraseMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> METHOD_CHANGE_PASSPHRASE = getChangePassphraseMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> getChangePassphraseMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> getChangePassphraseMethod() {
    return getChangePassphraseMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> getChangePassphraseMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest, com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> getChangePassphraseMethod;
    if ((getChangePassphraseMethod = WalletServiceGrpc.getChangePassphraseMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getChangePassphraseMethod = WalletServiceGrpc.getChangePassphraseMethod) == null) {
          WalletServiceGrpc.getChangePassphraseMethod = getChangePassphraseMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest, com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ChangePassphrase"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ChangePassphrase"))
                  .build();
          }
        }
     }
     return getChangePassphraseMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRenameAccountMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> METHOD_RENAME_ACCOUNT = getRenameAccountMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> getRenameAccountMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> getRenameAccountMethod() {
    return getRenameAccountMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> getRenameAccountMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> getRenameAccountMethod;
    if ((getRenameAccountMethod = WalletServiceGrpc.getRenameAccountMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getRenameAccountMethod = WalletServiceGrpc.getRenameAccountMethod) == null) {
          WalletServiceGrpc.getRenameAccountMethod = getRenameAccountMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "RenameAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("RenameAccount"))
                  .build();
          }
        }
     }
     return getRenameAccountMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRescanMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> METHOD_RESCAN = getRescanMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> getRescanMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> getRescanMethod() {
    return getRescanMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> getRescanMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest, com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> getRescanMethod;
    if ((getRescanMethod = WalletServiceGrpc.getRescanMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getRescanMethod = WalletServiceGrpc.getRescanMethod) == null) {
          WalletServiceGrpc.getRescanMethod = getRescanMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest, com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "Rescan"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("Rescan"))
                  .build();
          }
        }
     }
     return getRescanMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getNextAccountMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> METHOD_NEXT_ACCOUNT = getNextAccountMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> getNextAccountMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> getNextAccountMethod() {
    return getNextAccountMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> getNextAccountMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> getNextAccountMethod;
    if ((getNextAccountMethod = WalletServiceGrpc.getNextAccountMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getNextAccountMethod = WalletServiceGrpc.getNextAccountMethod) == null) {
          WalletServiceGrpc.getNextAccountMethod = getNextAccountMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "NextAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("NextAccount"))
                  .build();
          }
        }
     }
     return getNextAccountMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getNextAddressMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> METHOD_NEXT_ADDRESS = getNextAddressMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> getNextAddressMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> getNextAddressMethod() {
    return getNextAddressMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> getNextAddressMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> getNextAddressMethod;
    if ((getNextAddressMethod = WalletServiceGrpc.getNextAddressMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getNextAddressMethod = WalletServiceGrpc.getNextAddressMethod) == null) {
          WalletServiceGrpc.getNextAddressMethod = getNextAddressMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "NextAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("NextAddress"))
                  .build();
          }
        }
     }
     return getNextAddressMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getImportPrivateKeyMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> METHOD_IMPORT_PRIVATE_KEY = getImportPrivateKeyMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> getImportPrivateKeyMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> getImportPrivateKeyMethod() {
    return getImportPrivateKeyMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> getImportPrivateKeyMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest, com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> getImportPrivateKeyMethod;
    if ((getImportPrivateKeyMethod = WalletServiceGrpc.getImportPrivateKeyMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getImportPrivateKeyMethod = WalletServiceGrpc.getImportPrivateKeyMethod) == null) {
          WalletServiceGrpc.getImportPrivateKeyMethod = getImportPrivateKeyMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest, com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ImportPrivateKey"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ImportPrivateKey"))
                  .build();
          }
        }
     }
     return getImportPrivateKeyMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getImportScriptMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> METHOD_IMPORT_SCRIPT = getImportScriptMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> getImportScriptMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> getImportScriptMethod() {
    return getImportScriptMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> getImportScriptMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest, com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> getImportScriptMethod;
    if ((getImportScriptMethod = WalletServiceGrpc.getImportScriptMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getImportScriptMethod = WalletServiceGrpc.getImportScriptMethod) == null) {
          WalletServiceGrpc.getImportScriptMethod = getImportScriptMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest, com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ImportScript"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ImportScript"))
                  .build();
          }
        }
     }
     return getImportScriptMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFundTransactionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> METHOD_FUND_TRANSACTION = getFundTransactionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> getFundTransactionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> getFundTransactionMethod() {
    return getFundTransactionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> getFundTransactionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> getFundTransactionMethod;
    if ((getFundTransactionMethod = WalletServiceGrpc.getFundTransactionMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getFundTransactionMethod = WalletServiceGrpc.getFundTransactionMethod) == null) {
          WalletServiceGrpc.getFundTransactionMethod = getFundTransactionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "FundTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("FundTransaction"))
                  .build();
          }
        }
     }
     return getFundTransactionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getUnspentOutputsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> METHOD_UNSPENT_OUTPUTS = getUnspentOutputsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> getUnspentOutputsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> getUnspentOutputsMethod() {
    return getUnspentOutputsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> getUnspentOutputsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest, com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> getUnspentOutputsMethod;
    if ((getUnspentOutputsMethod = WalletServiceGrpc.getUnspentOutputsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getUnspentOutputsMethod = WalletServiceGrpc.getUnspentOutputsMethod) == null) {
          WalletServiceGrpc.getUnspentOutputsMethod = getUnspentOutputsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest, com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "UnspentOutputs"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("UnspentOutputs"))
                  .build();
          }
        }
     }
     return getUnspentOutputsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getConstructTransactionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> METHOD_CONSTRUCT_TRANSACTION = getConstructTransactionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> getConstructTransactionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> getConstructTransactionMethod() {
    return getConstructTransactionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> getConstructTransactionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> getConstructTransactionMethod;
    if ((getConstructTransactionMethod = WalletServiceGrpc.getConstructTransactionMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getConstructTransactionMethod = WalletServiceGrpc.getConstructTransactionMethod) == null) {
          WalletServiceGrpc.getConstructTransactionMethod = getConstructTransactionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ConstructTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ConstructTransaction"))
                  .build();
          }
        }
     }
     return getConstructTransactionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSignTransactionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> METHOD_SIGN_TRANSACTION = getSignTransactionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> getSignTransactionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> getSignTransactionMethod() {
    return getSignTransactionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> getSignTransactionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> getSignTransactionMethod;
    if ((getSignTransactionMethod = WalletServiceGrpc.getSignTransactionMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getSignTransactionMethod = WalletServiceGrpc.getSignTransactionMethod) == null) {
          WalletServiceGrpc.getSignTransactionMethod = getSignTransactionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "SignTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("SignTransaction"))
                  .build();
          }
        }
     }
     return getSignTransactionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSignTransactionsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> METHOD_SIGN_TRANSACTIONS = getSignTransactionsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> getSignTransactionsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> getSignTransactionsMethod() {
    return getSignTransactionsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> getSignTransactionsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> getSignTransactionsMethod;
    if ((getSignTransactionsMethod = WalletServiceGrpc.getSignTransactionsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getSignTransactionsMethod = WalletServiceGrpc.getSignTransactionsMethod) == null) {
          WalletServiceGrpc.getSignTransactionsMethod = getSignTransactionsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "SignTransactions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("SignTransactions"))
                  .build();
          }
        }
     }
     return getSignTransactionsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateSignatureMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> METHOD_CREATE_SIGNATURE = getCreateSignatureMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> getCreateSignatureMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> getCreateSignatureMethod() {
    return getCreateSignatureMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> getCreateSignatureMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> getCreateSignatureMethod;
    if ((getCreateSignatureMethod = WalletServiceGrpc.getCreateSignatureMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getCreateSignatureMethod = WalletServiceGrpc.getCreateSignatureMethod) == null) {
          WalletServiceGrpc.getCreateSignatureMethod = getCreateSignatureMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "CreateSignature"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("CreateSignature"))
                  .build();
          }
        }
     }
     return getCreateSignatureMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPublishTransactionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> METHOD_PUBLISH_TRANSACTION = getPublishTransactionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> getPublishTransactionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> getPublishTransactionMethod() {
    return getPublishTransactionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> getPublishTransactionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> getPublishTransactionMethod;
    if ((getPublishTransactionMethod = WalletServiceGrpc.getPublishTransactionMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getPublishTransactionMethod = WalletServiceGrpc.getPublishTransactionMethod) == null) {
          WalletServiceGrpc.getPublishTransactionMethod = getPublishTransactionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest, com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "PublishTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("PublishTransaction"))
                  .build();
          }
        }
     }
     return getPublishTransactionMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPublishUnminedTransactionsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> METHOD_PUBLISH_UNMINED_TRANSACTIONS = getPublishUnminedTransactionsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> getPublishUnminedTransactionsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> getPublishUnminedTransactionsMethod() {
    return getPublishUnminedTransactionsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> getPublishUnminedTransactionsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> getPublishUnminedTransactionsMethod;
    if ((getPublishUnminedTransactionsMethod = WalletServiceGrpc.getPublishUnminedTransactionsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getPublishUnminedTransactionsMethod = WalletServiceGrpc.getPublishUnminedTransactionsMethod) == null) {
          WalletServiceGrpc.getPublishUnminedTransactionsMethod = getPublishUnminedTransactionsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest, com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "PublishUnminedTransactions"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("PublishUnminedTransactions"))
                  .build();
          }
        }
     }
     return getPublishUnminedTransactionsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getPurchaseTicketsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> METHOD_PURCHASE_TICKETS = getPurchaseTicketsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> getPurchaseTicketsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> getPurchaseTicketsMethod() {
    return getPurchaseTicketsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> getPurchaseTicketsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> getPurchaseTicketsMethod;
    if ((getPurchaseTicketsMethod = WalletServiceGrpc.getPurchaseTicketsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getPurchaseTicketsMethod = WalletServiceGrpc.getPurchaseTicketsMethod) == null) {
          WalletServiceGrpc.getPurchaseTicketsMethod = getPurchaseTicketsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "PurchaseTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("PurchaseTickets"))
                  .build();
          }
        }
     }
     return getPurchaseTicketsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRevokeTicketsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> METHOD_REVOKE_TICKETS = getRevokeTicketsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> getRevokeTicketsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> getRevokeTicketsMethod() {
    return getRevokeTicketsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> getRevokeTicketsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> getRevokeTicketsMethod;
    if ((getRevokeTicketsMethod = WalletServiceGrpc.getRevokeTicketsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getRevokeTicketsMethod = WalletServiceGrpc.getRevokeTicketsMethod) == null) {
          WalletServiceGrpc.getRevokeTicketsMethod = getRevokeTicketsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "RevokeTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("RevokeTickets"))
                  .build();
          }
        }
     }
     return getRevokeTicketsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getLoadActiveDataFiltersMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> METHOD_LOAD_ACTIVE_DATA_FILTERS = getLoadActiveDataFiltersMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> getLoadActiveDataFiltersMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> getLoadActiveDataFiltersMethod() {
    return getLoadActiveDataFiltersMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> getLoadActiveDataFiltersMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest, com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> getLoadActiveDataFiltersMethod;
    if ((getLoadActiveDataFiltersMethod = WalletServiceGrpc.getLoadActiveDataFiltersMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getLoadActiveDataFiltersMethod = WalletServiceGrpc.getLoadActiveDataFiltersMethod) == null) {
          WalletServiceGrpc.getLoadActiveDataFiltersMethod = getLoadActiveDataFiltersMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest, com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "LoadActiveDataFilters"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("LoadActiveDataFilters"))
                  .build();
          }
        }
     }
     return getLoadActiveDataFiltersMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSignMessageMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> METHOD_SIGN_MESSAGE = getSignMessageMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> getSignMessageMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> getSignMessageMethod() {
    return getSignMessageMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> getSignMessageMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> getSignMessageMethod;
    if ((getSignMessageMethod = WalletServiceGrpc.getSignMessageMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getSignMessageMethod = WalletServiceGrpc.getSignMessageMethod) == null) {
          WalletServiceGrpc.getSignMessageMethod = getSignMessageMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "SignMessage"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("SignMessage"))
                  .build();
          }
        }
     }
     return getSignMessageMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSignMessagesMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> METHOD_SIGN_MESSAGES = getSignMessagesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> getSignMessagesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> getSignMessagesMethod() {
    return getSignMessagesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> getSignMessagesMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> getSignMessagesMethod;
    if ((getSignMessagesMethod = WalletServiceGrpc.getSignMessagesMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getSignMessagesMethod = WalletServiceGrpc.getSignMessagesMethod) == null) {
          WalletServiceGrpc.getSignMessagesMethod = getSignMessagesMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest, com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "SignMessages"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("SignMessages"))
                  .build();
          }
        }
     }
     return getSignMessagesMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getValidateAddressMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> METHOD_VALIDATE_ADDRESS = getValidateAddressMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> getValidateAddressMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> getValidateAddressMethod() {
    return getValidateAddressMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> getValidateAddressMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> getValidateAddressMethod;
    if ((getValidateAddressMethod = WalletServiceGrpc.getValidateAddressMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getValidateAddressMethod = WalletServiceGrpc.getValidateAddressMethod) == null) {
          WalletServiceGrpc.getValidateAddressMethod = getValidateAddressMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "ValidateAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("ValidateAddress"))
                  .build();
          }
        }
     }
     return getValidateAddressMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCommittedTicketsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> METHOD_COMMITTED_TICKETS = getCommittedTicketsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> getCommittedTicketsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> getCommittedTicketsMethod() {
    return getCommittedTicketsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> getCommittedTicketsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> getCommittedTicketsMethod;
    if ((getCommittedTicketsMethod = WalletServiceGrpc.getCommittedTicketsMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getCommittedTicketsMethod = WalletServiceGrpc.getCommittedTicketsMethod) == null) {
          WalletServiceGrpc.getCommittedTicketsMethod = getCommittedTicketsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest, com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "CommittedTickets"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("CommittedTickets"))
                  .build();
          }
        }
     }
     return getCommittedTicketsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSweepAccountMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> METHOD_SWEEP_ACCOUNT = getSweepAccountMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> getSweepAccountMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> getSweepAccountMethod() {
    return getSweepAccountMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> getSweepAccountMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> getSweepAccountMethod;
    if ((getSweepAccountMethod = WalletServiceGrpc.getSweepAccountMethod) == null) {
      synchronized (WalletServiceGrpc.class) {
        if ((getSweepAccountMethod = WalletServiceGrpc.getSweepAccountMethod) == null) {
          WalletServiceGrpc.getSweepAccountMethod = getSweepAccountMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletService", "SweepAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletServiceMethodDescriptorSupplier("SweepAccount"))
                  .build();
          }
        }
     }
     return getSweepAccountMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WalletServiceStub newStub(io.grpc.Channel channel) {
    return new WalletServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WalletServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new WalletServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WalletServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new WalletServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class WalletServiceImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public void ping(com.exrates.inout.service.decred.grpc.DecredApi.PingRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethodHelper(), responseObserver);
    }

    /**
     */
    public void network(com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNetworkMethodHelper(), responseObserver);
    }

    /**
     */
    public void accountNumber(com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAccountNumberMethodHelper(), responseObserver);
    }

    /**
     */
    public void accounts(com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAccountsMethodHelper(), responseObserver);
    }

    /**
     */
    public void balance(com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBalanceMethodHelper(), responseObserver);
    }

    /**
     */
    public void getAccountExtendedPubKey(com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountExtendedPubKeyMethodHelper(), responseObserver);
    }

    /**
     */
    public void getTransaction(com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTransactionMethodHelper(), responseObserver);
    }

    /**
     */
    public void getTransactions(com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTransactionsMethodHelper(), responseObserver);
    }

    /**
     */
    public void getTicket(com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTicketMethodHelper(), responseObserver);
    }

    /**
     */
    public void getTickets(com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTicketsMethodHelper(), responseObserver);
    }

    /**
     */
    public void ticketPrice(com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getTicketPriceMethodHelper(), responseObserver);
    }

    /**
     */
    public void stakeInfo(com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStakeInfoMethodHelper(), responseObserver);
    }

    /**
     */
    public void blockInfo(com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBlockInfoMethodHelper(), responseObserver);
    }

    /**
     */
    public void bestBlock(com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getBestBlockMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Notifications
     * </pre>
     */
    public void transactionNotifications(com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getTransactionNotificationsMethodHelper(), responseObserver);
    }

    /**
     */
    public void accountNotifications(com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAccountNotificationsMethodHelper(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest> confirmationNotifications(
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(getConfirmationNotificationsMethodHelper(), responseObserver);
    }

    /**
     * <pre>
     * Control
     * </pre>
     */
    public void changePassphrase(com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getChangePassphraseMethodHelper(), responseObserver);
    }

    /**
     */
    public void renameAccount(com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRenameAccountMethodHelper(), responseObserver);
    }

    /**
     */
    public void rescan(com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRescanMethodHelper(), responseObserver);
    }

    /**
     */
    public void nextAccount(com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNextAccountMethodHelper(), responseObserver);
    }

    /**
     */
    public void nextAddress(com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getNextAddressMethodHelper(), responseObserver);
    }

    /**
     */
    public void importPrivateKey(com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getImportPrivateKeyMethodHelper(), responseObserver);
    }

    /**
     */
    public void importScript(com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getImportScriptMethodHelper(), responseObserver);
    }

    /**
     */
    public void fundTransaction(com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFundTransactionMethodHelper(), responseObserver);
    }

    /**
     */
    public void unspentOutputs(com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUnspentOutputsMethodHelper(), responseObserver);
    }

    /**
     */
    public void constructTransaction(com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getConstructTransactionMethodHelper(), responseObserver);
    }

    /**
     */
    public void signTransaction(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSignTransactionMethodHelper(), responseObserver);
    }

    /**
     */
    public void signTransactions(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSignTransactionsMethodHelper(), responseObserver);
    }

    /**
     */
    public void createSignature(com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateSignatureMethodHelper(), responseObserver);
    }

    /**
     */
    public void publishTransaction(com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPublishTransactionMethodHelper(), responseObserver);
    }

    /**
     */
    public void publishUnminedTransactions(com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPublishUnminedTransactionsMethodHelper(), responseObserver);
    }

    /**
     */
    public void purchaseTickets(com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPurchaseTicketsMethodHelper(), responseObserver);
    }

    /**
     */
    public void revokeTickets(com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRevokeTicketsMethodHelper(), responseObserver);
    }

    /**
     */
    public void loadActiveDataFilters(com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getLoadActiveDataFiltersMethodHelper(), responseObserver);
    }

    /**
     */
    public void signMessage(com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSignMessageMethodHelper(), responseObserver);
    }

    /**
     */
    public void signMessages(com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSignMessagesMethodHelper(), responseObserver);
    }

    /**
     */
    public void validateAddress(com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getValidateAddressMethodHelper(), responseObserver);
    }

    /**
     */
    public void committedTickets(com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCommittedTicketsMethodHelper(), responseObserver);
    }

    /**
     */
    public void sweepAccount(com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSweepAccountMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.PingRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.PingResponse>(
                  this, METHODID_PING)))
          .addMethod(
            getNetworkMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse>(
                  this, METHODID_NETWORK)))
          .addMethod(
            getAccountNumberMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse>(
                  this, METHODID_ACCOUNT_NUMBER)))
          .addMethod(
            getAccountsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse>(
                  this, METHODID_ACCOUNTS)))
          .addMethod(
            getBalanceMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse>(
                  this, METHODID_BALANCE)))
          .addMethod(
            getGetAccountExtendedPubKeyMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse>(
                  this, METHODID_GET_ACCOUNT_EXTENDED_PUB_KEY)))
          .addMethod(
            getGetTransactionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse>(
                  this, METHODID_GET_TRANSACTION)))
          .addMethod(
            getGetTransactionsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse>(
                  this, METHODID_GET_TRANSACTIONS)))
          .addMethod(
            getGetTicketMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>(
                  this, METHODID_GET_TICKET)))
          .addMethod(
            getGetTicketsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>(
                  this, METHODID_GET_TICKETS)))
          .addMethod(
            getTicketPriceMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse>(
                  this, METHODID_TICKET_PRICE)))
          .addMethod(
            getStakeInfoMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse>(
                  this, METHODID_STAKE_INFO)))
          .addMethod(
            getBlockInfoMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse>(
                  this, METHODID_BLOCK_INFO)))
          .addMethod(
            getBestBlockMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse>(
                  this, METHODID_BEST_BLOCK)))
          .addMethod(
            getTransactionNotificationsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse>(
                  this, METHODID_TRANSACTION_NOTIFICATIONS)))
          .addMethod(
            getAccountNotificationsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse>(
                  this, METHODID_ACCOUNT_NOTIFICATIONS)))
          .addMethod(
            getConfirmationNotificationsMethodHelper(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse>(
                  this, METHODID_CONFIRMATION_NOTIFICATIONS)))
          .addMethod(
            getChangePassphraseMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse>(
                  this, METHODID_CHANGE_PASSPHRASE)))
          .addMethod(
            getRenameAccountMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse>(
                  this, METHODID_RENAME_ACCOUNT)))
          .addMethod(
            getRescanMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse>(
                  this, METHODID_RESCAN)))
          .addMethod(
            getNextAccountMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse>(
                  this, METHODID_NEXT_ACCOUNT)))
          .addMethod(
            getNextAddressMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse>(
                  this, METHODID_NEXT_ADDRESS)))
          .addMethod(
            getImportPrivateKeyMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse>(
                  this, METHODID_IMPORT_PRIVATE_KEY)))
          .addMethod(
            getImportScriptMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse>(
                  this, METHODID_IMPORT_SCRIPT)))
          .addMethod(
            getFundTransactionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse>(
                  this, METHODID_FUND_TRANSACTION)))
          .addMethod(
            getUnspentOutputsMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse>(
                  this, METHODID_UNSPENT_OUTPUTS)))
          .addMethod(
            getConstructTransactionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse>(
                  this, METHODID_CONSTRUCT_TRANSACTION)))
          .addMethod(
            getSignTransactionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse>(
                  this, METHODID_SIGN_TRANSACTION)))
          .addMethod(
            getSignTransactionsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse>(
                  this, METHODID_SIGN_TRANSACTIONS)))
          .addMethod(
            getCreateSignatureMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse>(
                  this, METHODID_CREATE_SIGNATURE)))
          .addMethod(
            getPublishTransactionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse>(
                  this, METHODID_PUBLISH_TRANSACTION)))
          .addMethod(
            getPublishUnminedTransactionsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse>(
                  this, METHODID_PUBLISH_UNMINED_TRANSACTIONS)))
          .addMethod(
            getPurchaseTicketsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse>(
                  this, METHODID_PURCHASE_TICKETS)))
          .addMethod(
            getRevokeTicketsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse>(
                  this, METHODID_REVOKE_TICKETS)))
          .addMethod(
            getLoadActiveDataFiltersMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse>(
                  this, METHODID_LOAD_ACTIVE_DATA_FILTERS)))
          .addMethod(
            getSignMessageMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse>(
                  this, METHODID_SIGN_MESSAGE)))
          .addMethod(
            getSignMessagesMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse>(
                  this, METHODID_SIGN_MESSAGES)))
          .addMethod(
            getValidateAddressMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse>(
                  this, METHODID_VALIDATE_ADDRESS)))
          .addMethod(
            getCommittedTicketsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse>(
                  this, METHODID_COMMITTED_TICKETS)))
          .addMethod(
            getSweepAccountMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse>(
                  this, METHODID_SWEEP_ACCOUNT)))
          .build();
    }
  }

  /**
   */
  public static final class WalletServiceStub extends io.grpc.stub.AbstractStub<WalletServiceStub> {
    private WalletServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public void ping(com.exrates.inout.service.decred.grpc.DecredApi.PingRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void network(com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNetworkMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void accountNumber(com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAccountNumberMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void accounts(com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAccountsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void balance(com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBalanceMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccountExtendedPubKey(com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountExtendedPubKeyMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTransaction(com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTransactionMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTransactions(com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetTransactionsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTicket(com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTicketMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTickets(com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getGetTicketsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ticketPrice(com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getTicketPriceMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stakeInfo(com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStakeInfoMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void blockInfo(com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBlockInfoMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void bestBlock(com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBestBlockMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Notifications
     * </pre>
     */
    public void transactionNotifications(com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getTransactionNotificationsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void accountNotifications(com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getAccountNotificationsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsRequest> confirmationNotifications(
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getConfirmationNotificationsMethodHelper(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Control
     * </pre>
     */
    public void changePassphrase(com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getChangePassphraseMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void renameAccount(com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRenameAccountMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rescan(com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getRescanMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void nextAccount(com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNextAccountMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void nextAddress(com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getNextAddressMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void importPrivateKey(com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getImportPrivateKeyMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void importScript(com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getImportScriptMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fundTransaction(com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFundTransactionMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void unspentOutputs(com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getUnspentOutputsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void constructTransaction(com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getConstructTransactionMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signTransaction(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSignTransactionMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signTransactions(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSignTransactionsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createSignature(com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateSignatureMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishTransaction(com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPublishTransactionMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void publishUnminedTransactions(com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPublishUnminedTransactionsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void purchaseTickets(com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPurchaseTicketsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void revokeTickets(com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRevokeTicketsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void loadActiveDataFilters(com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getLoadActiveDataFiltersMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signMessage(com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSignMessageMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void signMessages(com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSignMessagesMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void validateAddress(com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getValidateAddressMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void committedTickets(com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCommittedTicketsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sweepAccount(com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSweepAccountMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WalletServiceBlockingStub extends io.grpc.stub.AbstractStub<WalletServiceBlockingStub> {
    private WalletServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.PingResponse ping(com.exrates.inout.service.decred.grpc.DecredApi.PingRequest request) {
      return blockingUnaryCall(
          getChannel(), getPingMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse network(com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest request) {
      return blockingUnaryCall(
          getChannel(), getNetworkMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse accountNumber(com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest request) {
      return blockingUnaryCall(
          getChannel(), getAccountNumberMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse accounts(com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest request) {
      return blockingUnaryCall(
          getChannel(), getAccountsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse balance(com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getBalanceMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse getAccountExtendedPubKey(com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountExtendedPubKeyMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse getTransaction(com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetTransactionMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse> getTransactions(
        com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetTransactionsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse getTicket(com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetTicketMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getTickets(
        com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getGetTicketsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse ticketPrice(com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest request) {
      return blockingUnaryCall(
          getChannel(), getTicketPriceMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse stakeInfo(com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getStakeInfoMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse blockInfo(com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest request) {
      return blockingUnaryCall(
          getChannel(), getBlockInfoMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse bestBlock(com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest request) {
      return blockingUnaryCall(
          getChannel(), getBestBlockMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Notifications
     * </pre>
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse> transactionNotifications(
        com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getTransactionNotificationsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse> accountNotifications(
        com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getAccountNotificationsMethodHelper(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Control
     * </pre>
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse changePassphrase(com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest request) {
      return blockingUnaryCall(
          getChannel(), getChangePassphraseMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse renameAccount(com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getRenameAccountMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse> rescan(
        com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getRescanMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse nextAccount(com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getNextAccountMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse nextAddress(com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest request) {
      return blockingUnaryCall(
          getChannel(), getNextAddressMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse importPrivateKey(com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest request) {
      return blockingUnaryCall(
          getChannel(), getImportPrivateKeyMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse importScript(com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest request) {
      return blockingUnaryCall(
          getChannel(), getImportScriptMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse fundTransaction(com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getFundTransactionMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse> unspentOutputs(
        com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getUnspentOutputsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse constructTransaction(com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getConstructTransactionMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse signTransaction(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getSignTransactionMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse signTransactions(com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest request) {
      return blockingUnaryCall(
          getChannel(), getSignTransactionsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse createSignature(com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateSignatureMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse publishTransaction(com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getPublishTransactionMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse publishUnminedTransactions(com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest request) {
      return blockingUnaryCall(
          getChannel(), getPublishUnminedTransactionsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse purchaseTickets(com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest request) {
      return blockingUnaryCall(
          getChannel(), getPurchaseTicketsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse revokeTickets(com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest request) {
      return blockingUnaryCall(
          getChannel(), getRevokeTicketsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse loadActiveDataFilters(com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest request) {
      return blockingUnaryCall(
          getChannel(), getLoadActiveDataFiltersMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse signMessage(com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest request) {
      return blockingUnaryCall(
          getChannel(), getSignMessageMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse signMessages(com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest request) {
      return blockingUnaryCall(
          getChannel(), getSignMessagesMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse validateAddress(com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest request) {
      return blockingUnaryCall(
          getChannel(), getValidateAddressMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse committedTickets(com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest request) {
      return blockingUnaryCall(
          getChannel(), getCommittedTicketsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse sweepAccount(com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getSweepAccountMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WalletServiceFutureStub extends io.grpc.stub.AbstractStub<WalletServiceFutureStub> {
    private WalletServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Queries
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.PingResponse> ping(
        com.exrates.inout.service.decred.grpc.DecredApi.PingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse> network(
        com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNetworkMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse> accountNumber(
        com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAccountNumberMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse> accounts(
        com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAccountsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse> balance(
        com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBalanceMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse> getAccountExtendedPubKey(
        com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountExtendedPubKeyMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse> getTransaction(
        com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTransactionMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse> getTicket(
        com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTicketMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse> ticketPrice(
        com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getTicketPriceMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse> stakeInfo(
        com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStakeInfoMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse> blockInfo(
        com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBlockInfoMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse> bestBlock(
        com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getBestBlockMethodHelper(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Control
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse> changePassphrase(
        com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getChangePassphraseMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse> renameAccount(
        com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRenameAccountMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse> nextAccount(
        com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNextAccountMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse> nextAddress(
        com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getNextAddressMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse> importPrivateKey(
        com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getImportPrivateKeyMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse> importScript(
        com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getImportScriptMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse> fundTransaction(
        com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFundTransactionMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse> constructTransaction(
        com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getConstructTransactionMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse> signTransaction(
        com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSignTransactionMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse> signTransactions(
        com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSignTransactionsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse> createSignature(
        com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateSignatureMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse> publishTransaction(
        com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPublishTransactionMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse> publishUnminedTransactions(
        com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPublishUnminedTransactionsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse> purchaseTickets(
        com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPurchaseTicketsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse> revokeTickets(
        com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRevokeTicketsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse> loadActiveDataFilters(
        com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getLoadActiveDataFiltersMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse> signMessage(
        com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSignMessageMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse> signMessages(
        com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSignMessagesMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse> validateAddress(
        com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getValidateAddressMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse> committedTickets(
        com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCommittedTicketsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse> sweepAccount(
        com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSweepAccountMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_NETWORK = 1;
  private static final int METHODID_ACCOUNT_NUMBER = 2;
  private static final int METHODID_ACCOUNTS = 3;
  private static final int METHODID_BALANCE = 4;
  private static final int METHODID_GET_ACCOUNT_EXTENDED_PUB_KEY = 5;
  private static final int METHODID_GET_TRANSACTION = 6;
  private static final int METHODID_GET_TRANSACTIONS = 7;
  private static final int METHODID_GET_TICKET = 8;
  private static final int METHODID_GET_TICKETS = 9;
  private static final int METHODID_TICKET_PRICE = 10;
  private static final int METHODID_STAKE_INFO = 11;
  private static final int METHODID_BLOCK_INFO = 12;
  private static final int METHODID_BEST_BLOCK = 13;
  private static final int METHODID_TRANSACTION_NOTIFICATIONS = 14;
  private static final int METHODID_ACCOUNT_NOTIFICATIONS = 15;
  private static final int METHODID_CHANGE_PASSPHRASE = 16;
  private static final int METHODID_RENAME_ACCOUNT = 17;
  private static final int METHODID_RESCAN = 18;
  private static final int METHODID_NEXT_ACCOUNT = 19;
  private static final int METHODID_NEXT_ADDRESS = 20;
  private static final int METHODID_IMPORT_PRIVATE_KEY = 21;
  private static final int METHODID_IMPORT_SCRIPT = 22;
  private static final int METHODID_FUND_TRANSACTION = 23;
  private static final int METHODID_UNSPENT_OUTPUTS = 24;
  private static final int METHODID_CONSTRUCT_TRANSACTION = 25;
  private static final int METHODID_SIGN_TRANSACTION = 26;
  private static final int METHODID_SIGN_TRANSACTIONS = 27;
  private static final int METHODID_CREATE_SIGNATURE = 28;
  private static final int METHODID_PUBLISH_TRANSACTION = 29;
  private static final int METHODID_PUBLISH_UNMINED_TRANSACTIONS = 30;
  private static final int METHODID_PURCHASE_TICKETS = 31;
  private static final int METHODID_REVOKE_TICKETS = 32;
  private static final int METHODID_LOAD_ACTIVE_DATA_FILTERS = 33;
  private static final int METHODID_SIGN_MESSAGE = 34;
  private static final int METHODID_SIGN_MESSAGES = 35;
  private static final int METHODID_VALIDATE_ADDRESS = 36;
  private static final int METHODID_COMMITTED_TICKETS = 37;
  private static final int METHODID_SWEEP_ACCOUNT = 38;
  private static final int METHODID_CONFIRMATION_NOTIFICATIONS = 39;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WalletServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WalletServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((com.exrates.inout.service.decred.grpc.DecredApi.PingRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PingResponse>) responseObserver);
          break;
        case METHODID_NETWORK:
          serviceImpl.network((com.exrates.inout.service.decred.grpc.DecredApi.NetworkRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NetworkResponse>) responseObserver);
          break;
        case METHODID_ACCOUNT_NUMBER:
          serviceImpl.accountNumber((com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNumberResponse>) responseObserver);
          break;
        case METHODID_ACCOUNTS:
          serviceImpl.accounts((com.exrates.inout.service.decred.grpc.DecredApi.AccountsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountsResponse>) responseObserver);
          break;
        case METHODID_BALANCE:
          serviceImpl.balance((com.exrates.inout.service.decred.grpc.DecredApi.BalanceRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BalanceResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNT_EXTENDED_PUB_KEY:
          serviceImpl.getAccountExtendedPubKey((com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetAccountExtendedPubKeyResponse>) responseObserver);
          break;
        case METHODID_GET_TRANSACTION:
          serviceImpl.getTransaction((com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionResponse>) responseObserver);
          break;
        case METHODID_GET_TRANSACTIONS:
          serviceImpl.getTransactions((com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTransactionsResponse>) responseObserver);
          break;
        case METHODID_GET_TICKET:
          serviceImpl.getTicket((com.exrates.inout.service.decred.grpc.DecredApi.GetTicketRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>) responseObserver);
          break;
        case METHODID_GET_TICKETS:
          serviceImpl.getTickets((com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.GetTicketsResponse>) responseObserver);
          break;
        case METHODID_TICKET_PRICE:
          serviceImpl.ticketPrice((com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketPriceResponse>) responseObserver);
          break;
        case METHODID_STAKE_INFO:
          serviceImpl.stakeInfo((com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StakeInfoResponse>) responseObserver);
          break;
        case METHODID_BLOCK_INFO:
          serviceImpl.blockInfo((com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BlockInfoResponse>) responseObserver);
          break;
        case METHODID_BEST_BLOCK:
          serviceImpl.bestBlock((com.exrates.inout.service.decred.grpc.DecredApi.BestBlockRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.BestBlockResponse>) responseObserver);
          break;
        case METHODID_TRANSACTION_NOTIFICATIONS:
          serviceImpl.transactionNotifications((com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TransactionNotificationsResponse>) responseObserver);
          break;
        case METHODID_ACCOUNT_NOTIFICATIONS:
          serviceImpl.accountNotifications((com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AccountNotificationsResponse>) responseObserver);
          break;
        case METHODID_CHANGE_PASSPHRASE:
          serviceImpl.changePassphrase((com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ChangePassphraseResponse>) responseObserver);
          break;
        case METHODID_RENAME_ACCOUNT:
          serviceImpl.renameAccount((com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RenameAccountResponse>) responseObserver);
          break;
        case METHODID_RESCAN:
          serviceImpl.rescan((com.exrates.inout.service.decred.grpc.DecredApi.RescanRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanResponse>) responseObserver);
          break;
        case METHODID_NEXT_ACCOUNT:
          serviceImpl.nextAccount((com.exrates.inout.service.decred.grpc.DecredApi.NextAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAccountResponse>) responseObserver);
          break;
        case METHODID_NEXT_ADDRESS:
          serviceImpl.nextAddress((com.exrates.inout.service.decred.grpc.DecredApi.NextAddressRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.NextAddressResponse>) responseObserver);
          break;
        case METHODID_IMPORT_PRIVATE_KEY:
          serviceImpl.importPrivateKey((com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportPrivateKeyResponse>) responseObserver);
          break;
        case METHODID_IMPORT_SCRIPT:
          serviceImpl.importScript((com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ImportScriptResponse>) responseObserver);
          break;
        case METHODID_FUND_TRANSACTION:
          serviceImpl.fundTransaction((com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FundTransactionResponse>) responseObserver);
          break;
        case METHODID_UNSPENT_OUTPUTS:
          serviceImpl.unspentOutputs((com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.UnspentOutputResponse>) responseObserver);
          break;
        case METHODID_CONSTRUCT_TRANSACTION:
          serviceImpl.constructTransaction((com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConstructTransactionResponse>) responseObserver);
          break;
        case METHODID_SIGN_TRANSACTION:
          serviceImpl.signTransaction((com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionResponse>) responseObserver);
          break;
        case METHODID_SIGN_TRANSACTIONS:
          serviceImpl.signTransactions((com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignTransactionsResponse>) responseObserver);
          break;
        case METHODID_CREATE_SIGNATURE:
          serviceImpl.createSignature((com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateSignatureResponse>) responseObserver);
          break;
        case METHODID_PUBLISH_TRANSACTION:
          serviceImpl.publishTransaction((com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishTransactionResponse>) responseObserver);
          break;
        case METHODID_PUBLISH_UNMINED_TRANSACTIONS:
          serviceImpl.publishUnminedTransactions((com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PublishUnminedTransactionsResponse>) responseObserver);
          break;
        case METHODID_PURCHASE_TICKETS:
          serviceImpl.purchaseTickets((com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.PurchaseTicketsResponse>) responseObserver);
          break;
        case METHODID_REVOKE_TICKETS:
          serviceImpl.revokeTickets((com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RevokeTicketsResponse>) responseObserver);
          break;
        case METHODID_LOAD_ACTIVE_DATA_FILTERS:
          serviceImpl.loadActiveDataFilters((com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.LoadActiveDataFiltersResponse>) responseObserver);
          break;
        case METHODID_SIGN_MESSAGE:
          serviceImpl.signMessage((com.exrates.inout.service.decred.grpc.DecredApi.SignMessageRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessageResponse>) responseObserver);
          break;
        case METHODID_SIGN_MESSAGES:
          serviceImpl.signMessages((com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SignMessagesResponse>) responseObserver);
          break;
        case METHODID_VALIDATE_ADDRESS:
          serviceImpl.validateAddress((com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ValidateAddressResponse>) responseObserver);
          break;
        case METHODID_COMMITTED_TICKETS:
          serviceImpl.committedTickets((com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CommittedTicketsResponse>) responseObserver);
          break;
        case METHODID_SWEEP_ACCOUNT:
          serviceImpl.sweepAccount((com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SweepAccountResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CONFIRMATION_NOTIFICATIONS:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.confirmationNotifications(
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.ConfirmationNotificationsResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WalletServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WalletServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WalletService");
    }
  }

  private static final class WalletServiceFileDescriptorSupplier
      extends WalletServiceBaseDescriptorSupplier {
    WalletServiceFileDescriptorSupplier() {}
  }

  private static final class WalletServiceMethodDescriptorSupplier
      extends WalletServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WalletServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (WalletServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WalletServiceFileDescriptorSupplier())
              .addMethod(getPingMethodHelper())
              .addMethod(getNetworkMethodHelper())
              .addMethod(getAccountNumberMethodHelper())
              .addMethod(getAccountsMethodHelper())
              .addMethod(getBalanceMethodHelper())
              .addMethod(getGetAccountExtendedPubKeyMethodHelper())
              .addMethod(getGetTransactionMethodHelper())
              .addMethod(getGetTransactionsMethodHelper())
              .addMethod(getGetTicketMethodHelper())
              .addMethod(getGetTicketsMethodHelper())
              .addMethod(getTicketPriceMethodHelper())
              .addMethod(getStakeInfoMethodHelper())
              .addMethod(getBlockInfoMethodHelper())
              .addMethod(getBestBlockMethodHelper())
              .addMethod(getTransactionNotificationsMethodHelper())
              .addMethod(getAccountNotificationsMethodHelper())
              .addMethod(getConfirmationNotificationsMethodHelper())
              .addMethod(getChangePassphraseMethodHelper())
              .addMethod(getRenameAccountMethodHelper())
              .addMethod(getRescanMethodHelper())
              .addMethod(getNextAccountMethodHelper())
              .addMethod(getNextAddressMethodHelper())
              .addMethod(getImportPrivateKeyMethodHelper())
              .addMethod(getImportScriptMethodHelper())
              .addMethod(getFundTransactionMethodHelper())
              .addMethod(getUnspentOutputsMethodHelper())
              .addMethod(getConstructTransactionMethodHelper())
              .addMethod(getSignTransactionMethodHelper())
              .addMethod(getSignTransactionsMethodHelper())
              .addMethod(getCreateSignatureMethodHelper())
              .addMethod(getPublishTransactionMethodHelper())
              .addMethod(getPublishUnminedTransactionsMethodHelper())
              .addMethod(getPurchaseTicketsMethodHelper())
              .addMethod(getRevokeTicketsMethodHelper())
              .addMethod(getLoadActiveDataFiltersMethodHelper())
              .addMethod(getSignMessageMethodHelper())
              .addMethod(getSignMessagesMethodHelper())
              .addMethod(getValidateAddressMethodHelper())
              .addMethod(getCommittedTicketsMethodHelper())
              .addMethod(getSweepAccountMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

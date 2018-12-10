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
public final class WalletLoaderServiceGrpc {

  public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.WalletLoaderService";

  private WalletLoaderServiceGrpc() {}

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getWalletExistsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> METHOD_WALLET_EXISTS = getWalletExistsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> getWalletExistsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> getWalletExistsMethod() {
    return getWalletExistsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> getWalletExistsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest, com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> getWalletExistsMethod;
    if ((getWalletExistsMethod = WalletLoaderServiceGrpc.getWalletExistsMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getWalletExistsMethod = WalletLoaderServiceGrpc.getWalletExistsMethod) == null) {
          WalletLoaderServiceGrpc.getWalletExistsMethod = getWalletExistsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest, com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "WalletExists"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("WalletExists"))
                  .build();
          }
        }
     }
     return getWalletExistsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateWalletMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> METHOD_CREATE_WALLET = getCreateWalletMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> getCreateWalletMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> getCreateWalletMethod() {
    return getCreateWalletMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> getCreateWalletMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> getCreateWalletMethod;
    if ((getCreateWalletMethod = WalletLoaderServiceGrpc.getCreateWalletMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getCreateWalletMethod = WalletLoaderServiceGrpc.getCreateWalletMethod) == null) {
          WalletLoaderServiceGrpc.getCreateWalletMethod = getCreateWalletMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "CreateWallet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("CreateWallet"))
                  .build();
          }
        }
     }
     return getCreateWalletMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCreateWatchingOnlyWalletMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> METHOD_CREATE_WATCHING_ONLY_WALLET = getCreateWatchingOnlyWalletMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> getCreateWatchingOnlyWalletMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> getCreateWatchingOnlyWalletMethod() {
    return getCreateWatchingOnlyWalletMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> getCreateWatchingOnlyWalletMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> getCreateWatchingOnlyWalletMethod;
    if ((getCreateWatchingOnlyWalletMethod = WalletLoaderServiceGrpc.getCreateWatchingOnlyWalletMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getCreateWatchingOnlyWalletMethod = WalletLoaderServiceGrpc.getCreateWatchingOnlyWalletMethod) == null) {
          WalletLoaderServiceGrpc.getCreateWatchingOnlyWalletMethod = getCreateWatchingOnlyWalletMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "CreateWatchingOnlyWallet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("CreateWatchingOnlyWallet"))
                  .build();
          }
        }
     }
     return getCreateWatchingOnlyWalletMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getOpenWalletMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> METHOD_OPEN_WALLET = getOpenWalletMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> getOpenWalletMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> getOpenWalletMethod() {
    return getOpenWalletMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> getOpenWalletMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> getOpenWalletMethod;
    if ((getOpenWalletMethod = WalletLoaderServiceGrpc.getOpenWalletMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getOpenWalletMethod = WalletLoaderServiceGrpc.getOpenWalletMethod) == null) {
          WalletLoaderServiceGrpc.getOpenWalletMethod = getOpenWalletMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "OpenWallet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("OpenWallet"))
                  .build();
          }
        }
     }
     return getOpenWalletMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getCloseWalletMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> METHOD_CLOSE_WALLET = getCloseWalletMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> getCloseWalletMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> getCloseWalletMethod() {
    return getCloseWalletMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> getCloseWalletMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> getCloseWalletMethod;
    if ((getCloseWalletMethod = WalletLoaderServiceGrpc.getCloseWalletMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getCloseWalletMethod = WalletLoaderServiceGrpc.getCloseWalletMethod) == null) {
          WalletLoaderServiceGrpc.getCloseWalletMethod = getCloseWalletMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest, com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "CloseWallet"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("CloseWallet"))
                  .build();
          }
        }
     }
     return getCloseWalletMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStartConsensusRpcMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> METHOD_START_CONSENSUS_RPC = getStartConsensusRpcMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> getStartConsensusRpcMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> getStartConsensusRpcMethod() {
    return getStartConsensusRpcMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> getStartConsensusRpcMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest, com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> getStartConsensusRpcMethod;
    if ((getStartConsensusRpcMethod = WalletLoaderServiceGrpc.getStartConsensusRpcMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getStartConsensusRpcMethod = WalletLoaderServiceGrpc.getStartConsensusRpcMethod) == null) {
          WalletLoaderServiceGrpc.getStartConsensusRpcMethod = getStartConsensusRpcMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest, com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "StartConsensusRpc"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("StartConsensusRpc"))
                  .build();
          }
        }
     }
     return getStartConsensusRpcMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getDiscoverAddressesMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> METHOD_DISCOVER_ADDRESSES = getDiscoverAddressesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> getDiscoverAddressesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> getDiscoverAddressesMethod() {
    return getDiscoverAddressesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> getDiscoverAddressesMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest, com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> getDiscoverAddressesMethod;
    if ((getDiscoverAddressesMethod = WalletLoaderServiceGrpc.getDiscoverAddressesMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getDiscoverAddressesMethod = WalletLoaderServiceGrpc.getDiscoverAddressesMethod) == null) {
          WalletLoaderServiceGrpc.getDiscoverAddressesMethod = getDiscoverAddressesMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest, com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "DiscoverAddresses"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("DiscoverAddresses"))
                  .build();
          }
        }
     }
     return getDiscoverAddressesMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSubscribeToBlockNotificationsMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> METHOD_SUBSCRIBE_TO_BLOCK_NOTIFICATIONS = getSubscribeToBlockNotificationsMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> getSubscribeToBlockNotificationsMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> getSubscribeToBlockNotificationsMethod() {
    return getSubscribeToBlockNotificationsMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> getSubscribeToBlockNotificationsMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> getSubscribeToBlockNotificationsMethod;
    if ((getSubscribeToBlockNotificationsMethod = WalletLoaderServiceGrpc.getSubscribeToBlockNotificationsMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getSubscribeToBlockNotificationsMethod = WalletLoaderServiceGrpc.getSubscribeToBlockNotificationsMethod) == null) {
          WalletLoaderServiceGrpc.getSubscribeToBlockNotificationsMethod = getSubscribeToBlockNotificationsMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest, com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "SubscribeToBlockNotifications"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("SubscribeToBlockNotifications"))
                  .build();
          }
        }
     }
     return getSubscribeToBlockNotificationsMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFetchHeadersMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> METHOD_FETCH_HEADERS = getFetchHeadersMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> getFetchHeadersMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> getFetchHeadersMethod() {
    return getFetchHeadersMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> getFetchHeadersMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest, com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> getFetchHeadersMethod;
    if ((getFetchHeadersMethod = WalletLoaderServiceGrpc.getFetchHeadersMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getFetchHeadersMethod = WalletLoaderServiceGrpc.getFetchHeadersMethod) == null) {
          WalletLoaderServiceGrpc.getFetchHeadersMethod = getFetchHeadersMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest, com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "FetchHeaders"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("FetchHeaders"))
                  .build();
          }
        }
     }
     return getFetchHeadersMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getFetchMissingCFiltersMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> METHOD_FETCH_MISSING_CFILTERS = getFetchMissingCFiltersMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> getFetchMissingCFiltersMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> getFetchMissingCFiltersMethod() {
    return getFetchMissingCFiltersMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> getFetchMissingCFiltersMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest, com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> getFetchMissingCFiltersMethod;
    if ((getFetchMissingCFiltersMethod = WalletLoaderServiceGrpc.getFetchMissingCFiltersMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getFetchMissingCFiltersMethod = WalletLoaderServiceGrpc.getFetchMissingCFiltersMethod) == null) {
          WalletLoaderServiceGrpc.getFetchMissingCFiltersMethod = getFetchMissingCFiltersMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest, com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "FetchMissingCFilters"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("FetchMissingCFilters"))
                  .build();
          }
        }
     }
     return getFetchMissingCFiltersMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSpvSyncMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> METHOD_SPV_SYNC = getSpvSyncMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> getSpvSyncMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> getSpvSyncMethod() {
    return getSpvSyncMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> getSpvSyncMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest, com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> getSpvSyncMethod;
    if ((getSpvSyncMethod = WalletLoaderServiceGrpc.getSpvSyncMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getSpvSyncMethod = WalletLoaderServiceGrpc.getSpvSyncMethod) == null) {
          WalletLoaderServiceGrpc.getSpvSyncMethod = getSpvSyncMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest, com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "SpvSync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("SpvSync"))
                  .build();
          }
        }
     }
     return getSpvSyncMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRpcSyncMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> METHOD_RPC_SYNC = getRpcSyncMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> getRpcSyncMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> getRpcSyncMethod() {
    return getRpcSyncMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> getRpcSyncMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest, com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> getRpcSyncMethod;
    if ((getRpcSyncMethod = WalletLoaderServiceGrpc.getRpcSyncMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getRpcSyncMethod = WalletLoaderServiceGrpc.getRpcSyncMethod) == null) {
          WalletLoaderServiceGrpc.getRpcSyncMethod = getRpcSyncMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest, com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "RpcSync"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("RpcSync"))
                  .build();
          }
        }
     }
     return getRpcSyncMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRescanPointMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> METHOD_RESCAN_POINT = getRescanPointMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> getRescanPointMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> getRescanPointMethod() {
    return getRescanPointMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> getRescanPointMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest, com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> getRescanPointMethod;
    if ((getRescanPointMethod = WalletLoaderServiceGrpc.getRescanPointMethod) == null) {
      synchronized (WalletLoaderServiceGrpc.class) {
        if ((getRescanPointMethod = WalletLoaderServiceGrpc.getRescanPointMethod) == null) {
          WalletLoaderServiceGrpc.getRescanPointMethod = getRescanPointMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest, com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.WalletLoaderService", "RescanPoint"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new WalletLoaderServiceMethodDescriptorSupplier("RescanPoint"))
                  .build();
          }
        }
     }
     return getRescanPointMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static WalletLoaderServiceStub newStub(io.grpc.Channel channel) {
    return new WalletLoaderServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static WalletLoaderServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new WalletLoaderServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static WalletLoaderServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new WalletLoaderServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class WalletLoaderServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void walletExists(com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getWalletExistsMethodHelper(), responseObserver);
    }

    /**
     */
    public void createWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateWalletMethodHelper(), responseObserver);
    }

    /**
     */
    public void createWatchingOnlyWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCreateWatchingOnlyWalletMethodHelper(), responseObserver);
    }

    /**
     */
    public void openWallet(com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getOpenWalletMethodHelper(), responseObserver);
    }

    /**
     */
    public void closeWallet(com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getCloseWalletMethodHelper(), responseObserver);
    }

    /**
     */
    public void startConsensusRpc(com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStartConsensusRpcMethodHelper(), responseObserver);
    }

    /**
     */
    public void discoverAddresses(com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDiscoverAddressesMethodHelper(), responseObserver);
    }

    /**
     */
    public void subscribeToBlockNotifications(com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSubscribeToBlockNotificationsMethodHelper(), responseObserver);
    }

    /**
     */
    public void fetchHeaders(com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFetchHeadersMethodHelper(), responseObserver);
    }

    /**
     */
    public void fetchMissingCFilters(com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getFetchMissingCFiltersMethodHelper(), responseObserver);
    }

    /**
     */
    public void spvSync(com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSpvSyncMethodHelper(), responseObserver);
    }

    /**
     */
    public void rpcSync(com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRpcSyncMethodHelper(), responseObserver);
    }

    /**
     */
    public void rescanPoint(com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRescanPointMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getWalletExistsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse>(
                  this, METHODID_WALLET_EXISTS)))
          .addMethod(
            getCreateWalletMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse>(
                  this, METHODID_CREATE_WALLET)))
          .addMethod(
            getCreateWatchingOnlyWalletMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse>(
                  this, METHODID_CREATE_WATCHING_ONLY_WALLET)))
          .addMethod(
            getOpenWalletMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse>(
                  this, METHODID_OPEN_WALLET)))
          .addMethod(
            getCloseWalletMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse>(
                  this, METHODID_CLOSE_WALLET)))
          .addMethod(
            getStartConsensusRpcMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse>(
                  this, METHODID_START_CONSENSUS_RPC)))
          .addMethod(
            getDiscoverAddressesMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse>(
                  this, METHODID_DISCOVER_ADDRESSES)))
          .addMethod(
            getSubscribeToBlockNotificationsMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse>(
                  this, METHODID_SUBSCRIBE_TO_BLOCK_NOTIFICATIONS)))
          .addMethod(
            getFetchHeadersMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse>(
                  this, METHODID_FETCH_HEADERS)))
          .addMethod(
            getFetchMissingCFiltersMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse>(
                  this, METHODID_FETCH_MISSING_CFILTERS)))
          .addMethod(
            getSpvSyncMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse>(
                  this, METHODID_SPV_SYNC)))
          .addMethod(
            getRpcSyncMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse>(
                  this, METHODID_RPC_SYNC)))
          .addMethod(
            getRescanPointMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse>(
                  this, METHODID_RESCAN_POINT)))
          .build();
    }
  }

  /**
   */
  public static final class WalletLoaderServiceStub extends io.grpc.stub.AbstractStub<WalletLoaderServiceStub> {
    private WalletLoaderServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletLoaderServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletLoaderServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletLoaderServiceStub(channel, callOptions);
    }

    /**
     */
    public void walletExists(com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getWalletExistsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateWalletMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void createWatchingOnlyWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCreateWatchingOnlyWalletMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void openWallet(com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getOpenWalletMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void closeWallet(com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getCloseWalletMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void startConsensusRpc(com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStartConsensusRpcMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void discoverAddresses(com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDiscoverAddressesMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void subscribeToBlockNotifications(com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSubscribeToBlockNotificationsMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchHeaders(com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFetchHeadersMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void fetchMissingCFilters(com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFetchMissingCFiltersMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void spvSync(com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getSpvSyncMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rpcSync(com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getRpcSyncMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void rescanPoint(com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRescanPointMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class WalletLoaderServiceBlockingStub extends io.grpc.stub.AbstractStub<WalletLoaderServiceBlockingStub> {
    private WalletLoaderServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletLoaderServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletLoaderServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletLoaderServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse walletExists(com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest request) {
      return blockingUnaryCall(
          getChannel(), getWalletExistsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse createWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateWalletMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse createWatchingOnlyWallet(com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest request) {
      return blockingUnaryCall(
          getChannel(), getCreateWatchingOnlyWalletMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse openWallet(com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest request) {
      return blockingUnaryCall(
          getChannel(), getOpenWalletMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse closeWallet(com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest request) {
      return blockingUnaryCall(
          getChannel(), getCloseWalletMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse startConsensusRpc(com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest request) {
      return blockingUnaryCall(
          getChannel(), getStartConsensusRpcMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse discoverAddresses(com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest request) {
      return blockingUnaryCall(
          getChannel(), getDiscoverAddressesMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse subscribeToBlockNotifications(com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest request) {
      return blockingUnaryCall(
          getChannel(), getSubscribeToBlockNotificationsMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse fetchHeaders(com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest request) {
      return blockingUnaryCall(
          getChannel(), getFetchHeadersMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse fetchMissingCFilters(com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest request) {
      return blockingUnaryCall(
          getChannel(), getFetchMissingCFiltersMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse> spvSync(
        com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getSpvSyncMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse> rpcSync(
        com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getRpcSyncMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse rescanPoint(com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest request) {
      return blockingUnaryCall(
          getChannel(), getRescanPointMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class WalletLoaderServiceFutureStub extends io.grpc.stub.AbstractStub<WalletLoaderServiceFutureStub> {
    private WalletLoaderServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private WalletLoaderServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected WalletLoaderServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new WalletLoaderServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse> walletExists(
        com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getWalletExistsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse> createWallet(
        com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateWalletMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse> createWatchingOnlyWallet(
        com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCreateWatchingOnlyWalletMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse> openWallet(
        com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getOpenWalletMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse> closeWallet(
        com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getCloseWalletMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse> startConsensusRpc(
        com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStartConsensusRpcMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse> discoverAddresses(
        com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDiscoverAddressesMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse> subscribeToBlockNotifications(
        com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSubscribeToBlockNotificationsMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse> fetchHeaders(
        com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFetchHeadersMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse> fetchMissingCFilters(
        com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getFetchMissingCFiltersMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse> rescanPoint(
        com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getRescanPointMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_WALLET_EXISTS = 0;
  private static final int METHODID_CREATE_WALLET = 1;
  private static final int METHODID_CREATE_WATCHING_ONLY_WALLET = 2;
  private static final int METHODID_OPEN_WALLET = 3;
  private static final int METHODID_CLOSE_WALLET = 4;
  private static final int METHODID_START_CONSENSUS_RPC = 5;
  private static final int METHODID_DISCOVER_ADDRESSES = 6;
  private static final int METHODID_SUBSCRIBE_TO_BLOCK_NOTIFICATIONS = 7;
  private static final int METHODID_FETCH_HEADERS = 8;
  private static final int METHODID_FETCH_MISSING_CFILTERS = 9;
  private static final int METHODID_SPV_SYNC = 10;
  private static final int METHODID_RPC_SYNC = 11;
  private static final int METHODID_RESCAN_POINT = 12;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final WalletLoaderServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(WalletLoaderServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_WALLET_EXISTS:
          serviceImpl.walletExists((com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.WalletExistsResponse>) responseObserver);
          break;
        case METHODID_CREATE_WALLET:
          serviceImpl.createWallet((com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWalletResponse>) responseObserver);
          break;
        case METHODID_CREATE_WATCHING_ONLY_WALLET:
          serviceImpl.createWatchingOnlyWallet((com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CreateWatchingOnlyWalletResponse>) responseObserver);
          break;
        case METHODID_OPEN_WALLET:
          serviceImpl.openWallet((com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.OpenWalletResponse>) responseObserver);
          break;
        case METHODID_CLOSE_WALLET:
          serviceImpl.closeWallet((com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.CloseWalletResponse>) responseObserver);
          break;
        case METHODID_START_CONSENSUS_RPC:
          serviceImpl.startConsensusRpc((com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartConsensusRpcResponse>) responseObserver);
          break;
        case METHODID_DISCOVER_ADDRESSES:
          serviceImpl.discoverAddresses((com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.DiscoverAddressesResponse>) responseObserver);
          break;
        case METHODID_SUBSCRIBE_TO_BLOCK_NOTIFICATIONS:
          serviceImpl.subscribeToBlockNotifications((com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SubscribeToBlockNotificationsResponse>) responseObserver);
          break;
        case METHODID_FETCH_HEADERS:
          serviceImpl.fetchHeaders((com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchHeadersResponse>) responseObserver);
          break;
        case METHODID_FETCH_MISSING_CFILTERS:
          serviceImpl.fetchMissingCFilters((com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.FetchMissingCFiltersResponse>) responseObserver);
          break;
        case METHODID_SPV_SYNC:
          serviceImpl.spvSync((com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SpvSyncResponse>) responseObserver);
          break;
        case METHODID_RPC_SYNC:
          serviceImpl.rpcSync((com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RpcSyncResponse>) responseObserver);
          break;
        case METHODID_RESCAN_POINT:
          serviceImpl.rescanPoint((com.exrates.inout.service.decred.grpc.DecredApi.RescanPointRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RescanPointResponse>) responseObserver);
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
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class WalletLoaderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    WalletLoaderServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("WalletLoaderService");
    }
  }

  private static final class WalletLoaderServiceFileDescriptorSupplier
      extends WalletLoaderServiceBaseDescriptorSupplier {
    WalletLoaderServiceFileDescriptorSupplier() {}
  }

  private static final class WalletLoaderServiceMethodDescriptorSupplier
      extends WalletLoaderServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    WalletLoaderServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (WalletLoaderServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new WalletLoaderServiceFileDescriptorSupplier())
              .addMethod(getWalletExistsMethodHelper())
              .addMethod(getCreateWalletMethodHelper())
              .addMethod(getCreateWatchingOnlyWalletMethodHelper())
              .addMethod(getOpenWalletMethodHelper())
              .addMethod(getCloseWalletMethodHelper())
              .addMethod(getStartConsensusRpcMethodHelper())
              .addMethod(getDiscoverAddressesMethodHelper())
              .addMethod(getSubscribeToBlockNotificationsMethodHelper())
              .addMethod(getFetchHeadersMethodHelper())
              .addMethod(getFetchMissingCFiltersMethodHelper())
              .addMethod(getSpvSyncMethodHelper())
              .addMethod(getRpcSyncMethodHelper())
              .addMethod(getRescanPointMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

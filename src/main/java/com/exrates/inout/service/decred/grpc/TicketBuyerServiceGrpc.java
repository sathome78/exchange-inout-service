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
public final class TicketBuyerServiceGrpc {

  public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.TicketBuyerService";

  private TicketBuyerServiceGrpc() {}

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStartAutoBuyerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> METHOD_START_AUTO_BUYER = getStartAutoBuyerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> getStartAutoBuyerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> getStartAutoBuyerMethod() {
    return getStartAutoBuyerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> getStartAutoBuyerMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> getStartAutoBuyerMethod;
    if ((getStartAutoBuyerMethod = TicketBuyerServiceGrpc.getStartAutoBuyerMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getStartAutoBuyerMethod = TicketBuyerServiceGrpc.getStartAutoBuyerMethod) == null) {
          TicketBuyerServiceGrpc.getStartAutoBuyerMethod = getStartAutoBuyerMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "StartAutoBuyer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("StartAutoBuyer"))
                  .build();
          }
        }
     }
     return getStartAutoBuyerMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getStopAutoBuyerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> METHOD_STOP_AUTO_BUYER = getStopAutoBuyerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> getStopAutoBuyerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> getStopAutoBuyerMethod() {
    return getStopAutoBuyerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> getStopAutoBuyerMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> getStopAutoBuyerMethod;
    if ((getStopAutoBuyerMethod = TicketBuyerServiceGrpc.getStopAutoBuyerMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getStopAutoBuyerMethod = TicketBuyerServiceGrpc.getStopAutoBuyerMethod) == null) {
          TicketBuyerServiceGrpc.getStopAutoBuyerMethod = getStopAutoBuyerMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "StopAutoBuyer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("StopAutoBuyer"))
                  .build();
          }
        }
     }
     return getStopAutoBuyerMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getTicketBuyerConfigMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> METHOD_TICKET_BUYER_CONFIG = getTicketBuyerConfigMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> getTicketBuyerConfigMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> getTicketBuyerConfigMethod() {
    return getTicketBuyerConfigMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> getTicketBuyerConfigMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest, com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> getTicketBuyerConfigMethod;
    if ((getTicketBuyerConfigMethod = TicketBuyerServiceGrpc.getTicketBuyerConfigMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getTicketBuyerConfigMethod = TicketBuyerServiceGrpc.getTicketBuyerConfigMethod) == null) {
          TicketBuyerServiceGrpc.getTicketBuyerConfigMethod = getTicketBuyerConfigMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest, com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "TicketBuyerConfig"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("TicketBuyerConfig"))
                  .build();
          }
        }
     }
     return getTicketBuyerConfigMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetAccountMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> METHOD_SET_ACCOUNT = getSetAccountMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> getSetAccountMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> getSetAccountMethod() {
    return getSetAccountMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> getSetAccountMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> getSetAccountMethod;
    if ((getSetAccountMethod = TicketBuyerServiceGrpc.getSetAccountMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetAccountMethod = TicketBuyerServiceGrpc.getSetAccountMethod) == null) {
          TicketBuyerServiceGrpc.getSetAccountMethod = getSetAccountMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetAccount"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetAccount"))
                  .build();
          }
        }
     }
     return getSetAccountMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetBalanceToMaintainMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> METHOD_SET_BALANCE_TO_MAINTAIN = getSetBalanceToMaintainMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> getSetBalanceToMaintainMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> getSetBalanceToMaintainMethod() {
    return getSetBalanceToMaintainMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> getSetBalanceToMaintainMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> getSetBalanceToMaintainMethod;
    if ((getSetBalanceToMaintainMethod = TicketBuyerServiceGrpc.getSetBalanceToMaintainMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetBalanceToMaintainMethod = TicketBuyerServiceGrpc.getSetBalanceToMaintainMethod) == null) {
          TicketBuyerServiceGrpc.getSetBalanceToMaintainMethod = getSetBalanceToMaintainMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetBalanceToMaintain"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetBalanceToMaintain"))
                  .build();
          }
        }
     }
     return getSetBalanceToMaintainMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetMaxFeeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> METHOD_SET_MAX_FEE = getSetMaxFeeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> getSetMaxFeeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> getSetMaxFeeMethod() {
    return getSetMaxFeeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> getSetMaxFeeMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> getSetMaxFeeMethod;
    if ((getSetMaxFeeMethod = TicketBuyerServiceGrpc.getSetMaxFeeMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetMaxFeeMethod = TicketBuyerServiceGrpc.getSetMaxFeeMethod) == null) {
          TicketBuyerServiceGrpc.getSetMaxFeeMethod = getSetMaxFeeMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetMaxFee"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetMaxFee"))
                  .build();
          }
        }
     }
     return getSetMaxFeeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetMaxPriceRelativeMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> METHOD_SET_MAX_PRICE_RELATIVE = getSetMaxPriceRelativeMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> getSetMaxPriceRelativeMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> getSetMaxPriceRelativeMethod() {
    return getSetMaxPriceRelativeMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> getSetMaxPriceRelativeMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> getSetMaxPriceRelativeMethod;
    if ((getSetMaxPriceRelativeMethod = TicketBuyerServiceGrpc.getSetMaxPriceRelativeMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetMaxPriceRelativeMethod = TicketBuyerServiceGrpc.getSetMaxPriceRelativeMethod) == null) {
          TicketBuyerServiceGrpc.getSetMaxPriceRelativeMethod = getSetMaxPriceRelativeMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetMaxPriceRelative"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetMaxPriceRelative"))
                  .build();
          }
        }
     }
     return getSetMaxPriceRelativeMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetMaxPriceAbsoluteMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> METHOD_SET_MAX_PRICE_ABSOLUTE = getSetMaxPriceAbsoluteMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> getSetMaxPriceAbsoluteMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> getSetMaxPriceAbsoluteMethod() {
    return getSetMaxPriceAbsoluteMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> getSetMaxPriceAbsoluteMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> getSetMaxPriceAbsoluteMethod;
    if ((getSetMaxPriceAbsoluteMethod = TicketBuyerServiceGrpc.getSetMaxPriceAbsoluteMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetMaxPriceAbsoluteMethod = TicketBuyerServiceGrpc.getSetMaxPriceAbsoluteMethod) == null) {
          TicketBuyerServiceGrpc.getSetMaxPriceAbsoluteMethod = getSetMaxPriceAbsoluteMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetMaxPriceAbsolute"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetMaxPriceAbsolute"))
                  .build();
          }
        }
     }
     return getSetMaxPriceAbsoluteMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetVotingAddressMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> METHOD_SET_VOTING_ADDRESS = getSetVotingAddressMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> getSetVotingAddressMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> getSetVotingAddressMethod() {
    return getSetVotingAddressMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> getSetVotingAddressMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> getSetVotingAddressMethod;
    if ((getSetVotingAddressMethod = TicketBuyerServiceGrpc.getSetVotingAddressMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetVotingAddressMethod = TicketBuyerServiceGrpc.getSetVotingAddressMethod) == null) {
          TicketBuyerServiceGrpc.getSetVotingAddressMethod = getSetVotingAddressMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetVotingAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetVotingAddress"))
                  .build();
          }
        }
     }
     return getSetVotingAddressMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetPoolAddressMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> METHOD_SET_POOL_ADDRESS = getSetPoolAddressMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> getSetPoolAddressMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> getSetPoolAddressMethod() {
    return getSetPoolAddressMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> getSetPoolAddressMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> getSetPoolAddressMethod;
    if ((getSetPoolAddressMethod = TicketBuyerServiceGrpc.getSetPoolAddressMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetPoolAddressMethod = TicketBuyerServiceGrpc.getSetPoolAddressMethod) == null) {
          TicketBuyerServiceGrpc.getSetPoolAddressMethod = getSetPoolAddressMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetPoolAddress"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetPoolAddress"))
                  .build();
          }
        }
     }
     return getSetPoolAddressMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetPoolFeesMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> METHOD_SET_POOL_FEES = getSetPoolFeesMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> getSetPoolFeesMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> getSetPoolFeesMethod() {
    return getSetPoolFeesMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> getSetPoolFeesMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> getSetPoolFeesMethod;
    if ((getSetPoolFeesMethod = TicketBuyerServiceGrpc.getSetPoolFeesMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetPoolFeesMethod = TicketBuyerServiceGrpc.getSetPoolFeesMethod) == null) {
          TicketBuyerServiceGrpc.getSetPoolFeesMethod = getSetPoolFeesMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetPoolFees"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetPoolFees"))
                  .build();
          }
        }
     }
     return getSetPoolFeesMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getSetMaxPerBlockMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> METHOD_SET_MAX_PER_BLOCK = getSetMaxPerBlockMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> getSetMaxPerBlockMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> getSetMaxPerBlockMethod() {
    return getSetMaxPerBlockMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> getSetMaxPerBlockMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> getSetMaxPerBlockMethod;
    if ((getSetMaxPerBlockMethod = TicketBuyerServiceGrpc.getSetMaxPerBlockMethod) == null) {
      synchronized (TicketBuyerServiceGrpc.class) {
        if ((getSetMaxPerBlockMethod = TicketBuyerServiceGrpc.getSetMaxPerBlockMethod) == null) {
          TicketBuyerServiceGrpc.getSetMaxPerBlockMethod = getSetMaxPerBlockMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest, com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerService", "SetMaxPerBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerServiceMethodDescriptorSupplier("SetMaxPerBlock"))
                  .build();
          }
        }
     }
     return getSetMaxPerBlockMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TicketBuyerServiceStub newStub(io.grpc.Channel channel) {
    return new TicketBuyerServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TicketBuyerServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TicketBuyerServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TicketBuyerServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TicketBuyerServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TicketBuyerServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void startAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStartAutoBuyerMethodHelper(), responseObserver);
    }

    /**
     */
    public void stopAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getStopAutoBuyerMethodHelper(), responseObserver);
    }

    /**
     */
    public void ticketBuyerConfig(com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getTicketBuyerConfigMethodHelper(), responseObserver);
    }

    /**
     */
    public void setAccount(com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetAccountMethodHelper(), responseObserver);
    }

    /**
     */
    public void setBalanceToMaintain(com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetBalanceToMaintainMethodHelper(), responseObserver);
    }

    /**
     */
    public void setMaxFee(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMaxFeeMethodHelper(), responseObserver);
    }

    /**
     */
    public void setMaxPriceRelative(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMaxPriceRelativeMethodHelper(), responseObserver);
    }

    /**
     */
    public void setMaxPriceAbsolute(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMaxPriceAbsoluteMethodHelper(), responseObserver);
    }

    /**
     */
    public void setVotingAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetVotingAddressMethodHelper(), responseObserver);
    }

    /**
     */
    public void setPoolAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetPoolAddressMethodHelper(), responseObserver);
    }

    /**
     */
    public void setPoolFees(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetPoolFeesMethodHelper(), responseObserver);
    }

    /**
     */
    public void setMaxPerBlock(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSetMaxPerBlockMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getStartAutoBuyerMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse>(
                  this, METHODID_START_AUTO_BUYER)))
          .addMethod(
            getStopAutoBuyerMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse>(
                  this, METHODID_STOP_AUTO_BUYER)))
          .addMethod(
            getTicketBuyerConfigMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse>(
                  this, METHODID_TICKET_BUYER_CONFIG)))
          .addMethod(
            getSetAccountMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse>(
                  this, METHODID_SET_ACCOUNT)))
          .addMethod(
            getSetBalanceToMaintainMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse>(
                  this, METHODID_SET_BALANCE_TO_MAINTAIN)))
          .addMethod(
            getSetMaxFeeMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse>(
                  this, METHODID_SET_MAX_FEE)))
          .addMethod(
            getSetMaxPriceRelativeMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse>(
                  this, METHODID_SET_MAX_PRICE_RELATIVE)))
          .addMethod(
            getSetMaxPriceAbsoluteMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse>(
                  this, METHODID_SET_MAX_PRICE_ABSOLUTE)))
          .addMethod(
            getSetVotingAddressMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse>(
                  this, METHODID_SET_VOTING_ADDRESS)))
          .addMethod(
            getSetPoolAddressMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse>(
                  this, METHODID_SET_POOL_ADDRESS)))
          .addMethod(
            getSetPoolFeesMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse>(
                  this, METHODID_SET_POOL_FEES)))
          .addMethod(
            getSetMaxPerBlockMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse>(
                  this, METHODID_SET_MAX_PER_BLOCK)))
          .build();
    }
  }

  /**
   */
  public static final class TicketBuyerServiceStub extends io.grpc.stub.AbstractStub<TicketBuyerServiceStub> {
    private TicketBuyerServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerServiceStub(channel, callOptions);
    }

    /**
     */
    public void startAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStartAutoBuyerMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void stopAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getStopAutoBuyerMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ticketBuyerConfig(com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getTicketBuyerConfigMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setAccount(com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetAccountMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setBalanceToMaintain(com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetBalanceToMaintainMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMaxFee(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMaxFeeMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMaxPriceRelative(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMaxPriceRelativeMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMaxPriceAbsolute(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMaxPriceAbsoluteMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setVotingAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetVotingAddressMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setPoolAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetPoolAddressMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setPoolFees(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetPoolFeesMethodHelper(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void setMaxPerBlock(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSetMaxPerBlockMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TicketBuyerServiceBlockingStub extends io.grpc.stub.AbstractStub<TicketBuyerServiceBlockingStub> {
    private TicketBuyerServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse startAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest request) {
      return blockingUnaryCall(
          getChannel(), getStartAutoBuyerMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse stopAutoBuyer(com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest request) {
      return blockingUnaryCall(
          getChannel(), getStopAutoBuyerMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse ticketBuyerConfig(com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest request) {
      return blockingUnaryCall(
          getChannel(), getTicketBuyerConfigMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse setAccount(com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetAccountMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse setBalanceToMaintain(com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetBalanceToMaintainMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse setMaxFee(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMaxFeeMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse setMaxPriceRelative(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMaxPriceRelativeMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse setMaxPriceAbsolute(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMaxPriceAbsoluteMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse setVotingAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetVotingAddressMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse setPoolAddress(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetPoolAddressMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse setPoolFees(com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetPoolFeesMethodHelper(), getCallOptions(), request);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse setMaxPerBlock(com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest request) {
      return blockingUnaryCall(
          getChannel(), getSetMaxPerBlockMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TicketBuyerServiceFutureStub extends io.grpc.stub.AbstractStub<TicketBuyerServiceFutureStub> {
    private TicketBuyerServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse> startAutoBuyer(
        com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStartAutoBuyerMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse> stopAutoBuyer(
        com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getStopAutoBuyerMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse> ticketBuyerConfig(
        com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getTicketBuyerConfigMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse> setAccount(
        com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetAccountMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse> setBalanceToMaintain(
        com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetBalanceToMaintainMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse> setMaxFee(
        com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMaxFeeMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse> setMaxPriceRelative(
        com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMaxPriceRelativeMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse> setMaxPriceAbsolute(
        com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMaxPriceAbsoluteMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse> setVotingAddress(
        com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetVotingAddressMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse> setPoolAddress(
        com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetPoolAddressMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse> setPoolFees(
        com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetPoolFeesMethodHelper(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse> setMaxPerBlock(
        com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSetMaxPerBlockMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_START_AUTO_BUYER = 0;
  private static final int METHODID_STOP_AUTO_BUYER = 1;
  private static final int METHODID_TICKET_BUYER_CONFIG = 2;
  private static final int METHODID_SET_ACCOUNT = 3;
  private static final int METHODID_SET_BALANCE_TO_MAINTAIN = 4;
  private static final int METHODID_SET_MAX_FEE = 5;
  private static final int METHODID_SET_MAX_PRICE_RELATIVE = 6;
  private static final int METHODID_SET_MAX_PRICE_ABSOLUTE = 7;
  private static final int METHODID_SET_VOTING_ADDRESS = 8;
  private static final int METHODID_SET_POOL_ADDRESS = 9;
  private static final int METHODID_SET_POOL_FEES = 10;
  private static final int METHODID_SET_MAX_PER_BLOCK = 11;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TicketBuyerServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TicketBuyerServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_START_AUTO_BUYER:
          serviceImpl.startAutoBuyer((com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StartAutoBuyerResponse>) responseObserver);
          break;
        case METHODID_STOP_AUTO_BUYER:
          serviceImpl.stopAutoBuyer((com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.StopAutoBuyerResponse>) responseObserver);
          break;
        case METHODID_TICKET_BUYER_CONFIG:
          serviceImpl.ticketBuyerConfig((com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.TicketBuyerConfigResponse>) responseObserver);
          break;
        case METHODID_SET_ACCOUNT:
          serviceImpl.setAccount((com.exrates.inout.service.decred.grpc.DecredApi.SetAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetAccountResponse>) responseObserver);
          break;
        case METHODID_SET_BALANCE_TO_MAINTAIN:
          serviceImpl.setBalanceToMaintain((com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetBalanceToMaintainResponse>) responseObserver);
          break;
        case METHODID_SET_MAX_FEE:
          serviceImpl.setMaxFee((com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxFeeResponse>) responseObserver);
          break;
        case METHODID_SET_MAX_PRICE_RELATIVE:
          serviceImpl.setMaxPriceRelative((com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceRelativeResponse>) responseObserver);
          break;
        case METHODID_SET_MAX_PRICE_ABSOLUTE:
          serviceImpl.setMaxPriceAbsolute((com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPriceAbsoluteResponse>) responseObserver);
          break;
        case METHODID_SET_VOTING_ADDRESS:
          serviceImpl.setVotingAddress((com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetVotingAddressResponse>) responseObserver);
          break;
        case METHODID_SET_POOL_ADDRESS:
          serviceImpl.setPoolAddress((com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolAddressResponse>) responseObserver);
          break;
        case METHODID_SET_POOL_FEES:
          serviceImpl.setPoolFees((com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetPoolFeesResponse>) responseObserver);
          break;
        case METHODID_SET_MAX_PER_BLOCK:
          serviceImpl.setMaxPerBlock((com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.SetMaxPerBlockResponse>) responseObserver);
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

  private static abstract class TicketBuyerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TicketBuyerServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TicketBuyerService");
    }
  }

  private static final class TicketBuyerServiceFileDescriptorSupplier
      extends TicketBuyerServiceBaseDescriptorSupplier {
    TicketBuyerServiceFileDescriptorSupplier() {}
  }

  private static final class TicketBuyerServiceMethodDescriptorSupplier
      extends TicketBuyerServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TicketBuyerServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TicketBuyerServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TicketBuyerServiceFileDescriptorSupplier())
              .addMethod(getStartAutoBuyerMethodHelper())
              .addMethod(getStopAutoBuyerMethodHelper())
              .addMethod(getTicketBuyerConfigMethodHelper())
              .addMethod(getSetAccountMethodHelper())
              .addMethod(getSetBalanceToMaintainMethodHelper())
              .addMethod(getSetMaxFeeMethodHelper())
              .addMethod(getSetMaxPriceRelativeMethodHelper())
              .addMethod(getSetMaxPriceAbsoluteMethodHelper())
              .addMethod(getSetVotingAddressMethodHelper())
              .addMethod(getSetPoolAddressMethodHelper())
              .addMethod(getSetPoolFeesMethodHelper())
              .addMethod(getSetMaxPerBlockMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

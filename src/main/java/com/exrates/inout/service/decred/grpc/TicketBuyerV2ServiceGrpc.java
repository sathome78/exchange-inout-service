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
public final class TicketBuyerV2ServiceGrpc {

  public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.TicketBuyerV2Service";

  private TicketBuyerV2ServiceGrpc() {}

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getRunTicketBuyerMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> METHOD_RUN_TICKET_BUYER = getRunTicketBuyerMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> getRunTicketBuyerMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> getRunTicketBuyerMethod() {
    return getRunTicketBuyerMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> getRunTicketBuyerMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> getRunTicketBuyerMethod;
    if ((getRunTicketBuyerMethod = TicketBuyerV2ServiceGrpc.getRunTicketBuyerMethod) == null) {
      synchronized (TicketBuyerV2ServiceGrpc.class) {
        if ((getRunTicketBuyerMethod = TicketBuyerV2ServiceGrpc.getRunTicketBuyerMethod) == null) {
          TicketBuyerV2ServiceGrpc.getRunTicketBuyerMethod = getRunTicketBuyerMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest, com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.TicketBuyerV2Service", "RunTicketBuyer"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new TicketBuyerV2ServiceMethodDescriptorSupplier("RunTicketBuyer"))
                  .build();
          }
        }
     }
     return getRunTicketBuyerMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TicketBuyerV2ServiceStub newStub(io.grpc.Channel channel) {
    return new TicketBuyerV2ServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TicketBuyerV2ServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TicketBuyerV2ServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TicketBuyerV2ServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TicketBuyerV2ServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TicketBuyerV2ServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void runTicketBuyer(com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getRunTicketBuyerMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRunTicketBuyerMethodHelper(),
            asyncServerStreamingCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse>(
                  this, METHODID_RUN_TICKET_BUYER)))
          .build();
    }
  }

  /**
   */
  public static final class TicketBuyerV2ServiceStub extends io.grpc.stub.AbstractStub<TicketBuyerV2ServiceStub> {
    private TicketBuyerV2ServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerV2ServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerV2ServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerV2ServiceStub(channel, callOptions);
    }

    /**
     */
    public void runTicketBuyer(com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(getRunTicketBuyerMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TicketBuyerV2ServiceBlockingStub extends io.grpc.stub.AbstractStub<TicketBuyerV2ServiceBlockingStub> {
    private TicketBuyerV2ServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerV2ServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerV2ServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerV2ServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse> runTicketBuyer(
        com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest request) {
      return blockingServerStreamingCall(
          getChannel(), getRunTicketBuyerMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TicketBuyerV2ServiceFutureStub extends io.grpc.stub.AbstractStub<TicketBuyerV2ServiceFutureStub> {
    private TicketBuyerV2ServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TicketBuyerV2ServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected TicketBuyerV2ServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TicketBuyerV2ServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_RUN_TICKET_BUYER = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TicketBuyerV2ServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TicketBuyerV2ServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_RUN_TICKET_BUYER:
          serviceImpl.runTicketBuyer((com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.RunTicketBuyerResponse>) responseObserver);
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

  private static abstract class TicketBuyerV2ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    TicketBuyerV2ServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("TicketBuyerV2Service");
    }
  }

  private static final class TicketBuyerV2ServiceFileDescriptorSupplier
      extends TicketBuyerV2ServiceBaseDescriptorSupplier {
    TicketBuyerV2ServiceFileDescriptorSupplier() {}
  }

  private static final class TicketBuyerV2ServiceMethodDescriptorSupplier
      extends TicketBuyerV2ServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    TicketBuyerV2ServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (TicketBuyerV2ServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TicketBuyerV2ServiceFileDescriptorSupplier())
              .addMethod(getRunTicketBuyerMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

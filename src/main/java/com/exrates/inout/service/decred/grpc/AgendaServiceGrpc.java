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
public final class AgendaServiceGrpc {

    public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.AgendaService";

  private AgendaServiceGrpc() {}

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getAgendasMethod()} instead.
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> METHOD_AGENDAS = getAgendasMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> getAgendasMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> getAgendasMethod() {
    return getAgendasMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> getAgendasMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest, com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> getAgendasMethod;
    if ((getAgendasMethod = AgendaServiceGrpc.getAgendasMethod) == null) {
      synchronized (AgendaServiceGrpc.class) {
        if ((getAgendasMethod = AgendaServiceGrpc.getAgendasMethod) == null) {
          AgendaServiceGrpc.getAgendasMethod = getAgendasMethod =
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest, com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.AgendaService", "Agendas"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AgendaServiceMethodDescriptorSupplier("Agendas"))
                  .build();
          }
        }
     }
     return getAgendasMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AgendaServiceStub newStub(io.grpc.Channel channel) {
    return new AgendaServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AgendaServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AgendaServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AgendaServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AgendaServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class AgendaServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void agendas(com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getAgendasMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getAgendasMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse>(
                  this, METHODID_AGENDAS)))
          .build();
    }
  }

  /**
   */
  public static final class AgendaServiceStub extends io.grpc.stub.AbstractStub<AgendaServiceStub> {
    private AgendaServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgendaServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgendaServiceStub(channel, callOptions);
    }

    /**
     */
    public void agendas(com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getAgendasMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class AgendaServiceBlockingStub extends io.grpc.stub.AbstractStub<AgendaServiceBlockingStub> {
    private AgendaServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgendaServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgendaServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse agendas(com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest request) {
      return blockingUnaryCall(
          getChannel(), getAgendasMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class AgendaServiceFutureStub extends io.grpc.stub.AbstractStub<AgendaServiceFutureStub> {
    private AgendaServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AgendaServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AgendaServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AgendaServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse> agendas(
        com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getAgendasMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_AGENDAS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AgendaServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AgendaServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_AGENDAS:
          serviceImpl.agendas((com.exrates.inout.service.decred.grpc.DecredApi.AgendasRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.AgendasResponse>) responseObserver);
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

  private static abstract class AgendaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AgendaServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AgendaService");
    }
  }

  private static final class AgendaServiceFileDescriptorSupplier
      extends AgendaServiceBaseDescriptorSupplier {
    AgendaServiceFileDescriptorSupplier() {}
  }

  private static final class AgendaServiceMethodDescriptorSupplier
      extends AgendaServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AgendaServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (AgendaServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AgendaServiceFileDescriptorSupplier())
              .addMethod(getAgendasMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

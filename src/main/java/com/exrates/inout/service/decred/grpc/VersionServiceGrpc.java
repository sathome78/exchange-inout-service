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
public final class VersionServiceGrpc {

  public static final String SERVICE_NAME = "com.exrates.inout.service.decred.grpc.VersionService";

  private VersionServiceGrpc() {}

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @java.lang.Deprecated // Use {@link #getVersionMethod()} instead. 
  public static final io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> METHOD_VERSION = getVersionMethodHelper();

  private static volatile io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> getVersionMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> getVersionMethod() {
    return getVersionMethodHelper();
  }

  private static io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest,
      com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> getVersionMethodHelper() {
    io.grpc.MethodDescriptor<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest, com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> getVersionMethod;
    if ((getVersionMethod = VersionServiceGrpc.getVersionMethod) == null) {
      synchronized (VersionServiceGrpc.class) {
        if ((getVersionMethod = VersionServiceGrpc.getVersionMethod) == null) {
          VersionServiceGrpc.getVersionMethod = getVersionMethod = 
              io.grpc.MethodDescriptor.<com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest, com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.exrates.inout.service.decred.grpc.VersionService", "Version"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new VersionServiceMethodDescriptorSupplier("Version"))
                  .build();
          }
        }
     }
     return getVersionMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static VersionServiceStub newStub(io.grpc.Channel channel) {
    return new VersionServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static VersionServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new VersionServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static VersionServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new VersionServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class VersionServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void version(com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getVersionMethodHelper(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getVersionMethodHelper(),
            asyncUnaryCall(
              new MethodHandlers<
                com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest,
                com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse>(
                  this, METHODID_VERSION)))
          .build();
    }
  }

  /**
   */
  public static final class VersionServiceStub extends io.grpc.stub.AbstractStub<VersionServiceStub> {
    private VersionServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VersionServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VersionServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VersionServiceStub(channel, callOptions);
    }

    /**
     */
    public void version(com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest request,
        io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getVersionMethodHelper(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class VersionServiceBlockingStub extends io.grpc.stub.AbstractStub<VersionServiceBlockingStub> {
    private VersionServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VersionServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VersionServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VersionServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse version(com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest request) {
      return blockingUnaryCall(
          getChannel(), getVersionMethodHelper(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class VersionServiceFutureStub extends io.grpc.stub.AbstractStub<VersionServiceFutureStub> {
    private VersionServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private VersionServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected VersionServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new VersionServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse> version(
        com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getVersionMethodHelper(), getCallOptions()), request);
    }
  }

  private static final int METHODID_VERSION = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final VersionServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(VersionServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VERSION:
          serviceImpl.version((com.exrates.inout.service.decred.grpc.DecredApi.VersionRequest) request,
              (io.grpc.stub.StreamObserver<com.exrates.inout.service.decred.grpc.DecredApi.VersionResponse>) responseObserver);
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

  private static abstract class VersionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    VersionServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.exrates.inout.service.decred.grpc.DecredApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("VersionService");
    }
  }

  private static final class VersionServiceFileDescriptorSupplier
      extends VersionServiceBaseDescriptorSupplier {
    VersionServiceFileDescriptorSupplier() {}
  }

  private static final class VersionServiceMethodDescriptorSupplier
      extends VersionServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    VersionServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (VersionServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new VersionServiceFileDescriptorSupplier())
              .addMethod(getVersionMethodHelper())
              .build();
        }
      }
    }
    return result;
  }
}

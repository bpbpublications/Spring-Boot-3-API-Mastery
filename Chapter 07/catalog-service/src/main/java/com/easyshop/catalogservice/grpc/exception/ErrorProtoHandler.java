package com.easyshop.catalogservice.grpc.exception;

import com.easyshop.catalogservice.exception.ProductNotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ErrorProtoHandler {

    @GrpcExceptionHandler
    public StatusRuntimeException handle(ProductNotFoundException ex) {
        var status = Status.NOT_FOUND.withDescription(ex.getMessage()).withCause(ex);
        return status.asRuntimeException();
    }
}

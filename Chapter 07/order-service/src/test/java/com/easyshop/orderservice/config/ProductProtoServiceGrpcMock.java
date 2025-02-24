package com.easyshop.orderservice.config;

import com.easyshop.catalogservice.proto.ProductProtoRequest;
import com.easyshop.catalogservice.proto.ProductProtoResponse;
import com.easyshop.catalogservice.proto.ProductProtoServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ProductProtoServiceGrpcMock extends ProductProtoServiceGrpc.ProductProtoServiceImplBase {



    @Override
    public void findProductByCode(ProductProtoRequest request,
                          StreamObserver<ProductProtoResponse> responseObserver) {

        if(request.getCode().equals("not-found-code")) {
            var status = Status.NOT_FOUND.withDescription("Product not Found");
            responseObserver.onError(status.asException());
        }
        else {
            var response = ProductProtoResponse.newBuilder()
                    .setCode(request.getCode())
                    .setCategory("laptop")
                    .setPrice(50000L)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    }

}

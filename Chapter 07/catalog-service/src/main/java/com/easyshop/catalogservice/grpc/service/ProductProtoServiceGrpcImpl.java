package com.easyshop.catalogservice.grpc.service;

import com.easyshop.catalogservice.proto.ProductProtoRequest;
import com.easyshop.catalogservice.proto.ProductProtoResponse;
import com.easyshop.catalogservice.proto.ReactorProductProtoServiceGrpc;
import com.easyshop.catalogservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;


@GrpcService
@RequiredArgsConstructor
@Slf4j
public class ProductProtoServiceGrpcImpl extends ReactorProductProtoServiceGrpc.ProductProtoServiceImplBase {

    private final ProductService productService;

    @Override
    public Mono<ProductProtoResponse> findProductByCode(Mono<ProductProtoRequest> request) {
        return request
                .doOnNext(protoRequest -> log.info("Proto request: {}", protoRequest))
                .flatMap(protoRequest -> productService.findProductByCode(protoRequest.getCode()))
                .map(productResponse -> ProductProtoResponse.newBuilder()
                        .setCode(productResponse.getCode())
                        .setCategory(productResponse.getCategory().getValue())
                        .setPrice(productResponse.getPrice())
                        .build())
                .doOnNext(protoResponse -> log.info("Proto response: {}", protoResponse))
                .doOnError(ex -> log.error("Error in findProductByCode with request: {}", request, ex));
    }

}

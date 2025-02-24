package com.easyshop.orderservice.middleware.msclient.impl;

import com.easyshop.catalogservice.proto.ProductProtoRequest;
import com.easyshop.catalogservice.proto.ProductProtoServiceGrpc;
import com.easyshop.orderservice.exception.ProductOrderNotFoundException;
import com.easyshop.orderservice.middleware.msclient.ProductClient;
import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "order.service.product-client-type", havingValue = "grpc")
@Slf4j
public class GrpcProductClient implements ProductClient {

    private final ProductProtoServiceGrpc.ProductProtoServiceBlockingStub productProtoService;

    public GrpcProductClient(@GrpcClient("productProtoService")
                          ProductProtoServiceGrpc.ProductProtoServiceBlockingStub productProtoService) {
        this.productProtoService = productProtoService;
    }

    @Override
    public ProductResponse findByProductCode(String productCode) {
        var request = ProductProtoRequest.newBuilder().setCode(productCode).build();
        try {
            var product = productProtoService.findProductByCode(request);
            log.debug("Product with code {} found", product.getCode());
            return new ProductResponse(
                    product.getCode(),
                    product.getCategory(),
                    product.getPrice()
            );
        }
        catch (StatusRuntimeException ex) {
            if(Status.NOT_FOUND.getCode().equals(ex.getStatus().getCode())) {
                throw new ProductOrderNotFoundException(productCode);
            }
            throw ex;
        }
    }
}

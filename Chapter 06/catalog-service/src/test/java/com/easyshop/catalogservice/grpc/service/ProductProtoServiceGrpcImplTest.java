package com.easyshop.catalogservice.grpc.service;

import com.easyshop.catalogservice.exception.ProductNotFoundException;
import com.easyshop.catalogservice.generated.model.ProductCategory;
import com.easyshop.catalogservice.generated.model.ProductResponse;
import com.easyshop.catalogservice.proto.ProductProtoRequest;
import com.easyshop.catalogservice.proto.ProductProtoResponse;
import com.easyshop.catalogservice.proto.ReactorProductProtoServiceGrpc;
import com.easyshop.catalogservice.service.ProductService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

//https://yidongnan.github.io/grpc-spring-boot-starter/en/server/testing.html
@SpringBootTest(properties = {
        "grpc.server.in-process-name=test",
        "grpc.server.port=-1",
        "grpc.client.inProcess.address=in-process:test"
})
class ProductProtoServiceGrpcImplTest {

    @GrpcClient("inProcess")
    private ReactorProductProtoServiceGrpc.ReactorProductProtoServiceStub protoServiceStub;

    @MockBean
    private ProductService productService;

    @Test
    void findProductByCodeOkTest() {
        var productCode = "0001";
        var category = ProductCategory.LAPTOP;
        var price = 50000L;
        var request = ProductProtoRequest.newBuilder()
                .setCode(productCode)
                .build();

        var expectedResponse = ProductProtoResponse.newBuilder()
                .setCode(productCode)
                .setCategory(category.getValue())
                .setPrice(price)
                .build();

        when(productService.findProductByCode(productCode))
                .thenReturn(Mono.just(new ProductResponse()
                        .code(productCode)
                        .category(category)
                        .price(price)));


        StepVerifier.create(protoServiceStub.findProductByCode(request))
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void findProductByCodeKoTest() {
        var productCode = "0001";
        var request = ProductProtoRequest.newBuilder()
                .setCode(productCode)
                .build();

        when(productService.findProductByCode(productCode))
                .thenReturn(Mono.error(new ProductNotFoundException("0001")));


        StepVerifier.create(protoServiceStub.findProductByCode(request))
                .expectErrorMatches(ex -> ex instanceof StatusRuntimeException srEx &&
                        srEx.getStatus().getCode().equals(Status.Code.NOT_FOUND))
                .verify();
    }


}

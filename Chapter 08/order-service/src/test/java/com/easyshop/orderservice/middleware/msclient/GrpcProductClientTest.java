package com.easyshop.orderservice.middleware.msclient;

import com.easyshop.orderservice.config.GrpcTestConfig;
import com.easyshop.orderservice.exception.ProductOrderNotFoundException;
import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(properties = {
        "grpc.server.inProcessName=test", // Enable inProcess server
        "grpc.server.port=-1", // Disable external server
        "grpc.client.productProtoService.address=in-process:test" // Configure the client to connect to the inProcess server
})
@SpringJUnitConfig(classes = { GrpcTestConfig.class })
class GrpcProductClientTest {

    @Autowired
    private ProductClient productClient;

    @Test
    void validateProductCodeOkTest() {
        var expectedResponse = new ProductResponse("0001", "laptop", 50000L);
        var actualResponse = productClient.findByProductCode("0001");
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void validateProductCodeKoTest() {
        assertThatExceptionOfType(ProductOrderNotFoundException.class)
                .isThrownBy(() -> productClient.findByProductCode("not-found-code"));
    }
}

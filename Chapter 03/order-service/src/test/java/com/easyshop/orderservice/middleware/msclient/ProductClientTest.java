package com.easyshop.orderservice.middleware.msclient;

import com.easyshop.orderservice.config.OrderServiceProperties;
import com.easyshop.orderservice.exception.ProductOrderNotFoundException;
import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withResourceNotFound;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest(ProductClient.class)
@Import(OrderServiceProperties.class)
class ProductClientTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void findByProductCodeOk() throws JsonProcessingException {
        var expectedResponse = new ProductResponse("code1", "laptop", 10000L);
        var responseJson = objectMapper.writeValueAsString(expectedResponse);
        server.expect(requestTo("http://localhost:8080/products/code1"))
                .andRespond(
                        withSuccess(responseJson, MediaType.APPLICATION_JSON)
                );

        var actualResponse = productClient.findByProductCode("code1");
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void findByProductCodeKo() {
        server.expect(requestTo("http://localhost:8080/products/code1"))
                .andRespond(withResourceNotFound());

        assertThatExceptionOfType(ProductOrderNotFoundException.class)
                .isThrownBy(() -> productClient.findByProductCode("code1"));
    }
}

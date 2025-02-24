package com.easyshop.orderservice.middleware.msclient.impl;

import com.easyshop.orderservice.config.OrderServiceProperties;
import com.easyshop.orderservice.exception.ProductOrderNotFoundException;
import com.easyshop.orderservice.middleware.msclient.ProductClient;
import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientProductClient implements ProductClient {

    private final RestClient restClient;

    public RestClientProductClient(RestClient.Builder builder,
                                   OrderServiceProperties properties) {
        restClient = builder
                .baseUrl(properties.getCatalogserviceUrl())
                .build();
    }

    @Override
    public ProductResponse findByProductCode(String productCode) {
        return restClient.get()
                .uri("/" + productCode)
                .retrieve()
                .onStatus(status -> status.value() == 404, (request, response) -> {
                    throw new ProductOrderNotFoundException(productCode);
                })
                .body(new ParameterizedTypeReference<>() {});
    }
}

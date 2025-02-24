package com.easyshop.orderservice.middleware.msclient.impl;

import com.easyshop.orderservice.config.OrderServiceProperties;
import com.easyshop.orderservice.middleware.msclient.ShipmentClient;
import com.easyshop.orderservice.middleware.msclient.dto.ShipmentRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class RestClientShipmentClient implements ShipmentClient {

    private final RestClient restClient;

    public RestClientShipmentClient(RestClient.Builder builder,
                                   OrderServiceProperties properties) {
        restClient = builder
                .baseUrl(properties.getShipmentServiceUrl())
                .build();
    }

    @Override
    public void addShipment(ShipmentRequest request) {
        restClient.post()
                .body(request)
                .retrieve()
                .toBodilessEntity();
    }
}

package com.easyshop.orderservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "order.service")
@Data
public class OrderServiceProperties {

    private String catalogserviceUrl;
    private String shipmentServiceUrl;
    private ProductClientType productClientType;


    public enum ProductClientType {
        REST,
        GRPC
    }
}

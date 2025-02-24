package com.easyshop.orderservice.middleware.msclient.impl;

import com.easyshop.orderservice.middleware.msclient.ProductClient;
import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;
import org.springframework.stereotype.Component;

//@Component
public class MockProductClient implements ProductClient {
    @Override
    public ProductResponse findByProductCode(String productCode) {
        return new ProductResponse(productCode, null, 10000L);
    }
}

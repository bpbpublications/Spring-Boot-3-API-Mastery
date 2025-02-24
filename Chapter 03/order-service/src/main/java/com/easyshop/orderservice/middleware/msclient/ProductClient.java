package com.easyshop.orderservice.middleware.msclient;

import com.easyshop.orderservice.middleware.msclient.dto.ProductResponse;

public interface ProductClient {

    ProductResponse findByProductCode(String productCode);
}

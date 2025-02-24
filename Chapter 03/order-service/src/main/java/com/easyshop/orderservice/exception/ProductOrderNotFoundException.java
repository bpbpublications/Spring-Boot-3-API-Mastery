package com.easyshop.orderservice.exception;

import org.springframework.core.NestedRuntimeException;

public class ProductOrderNotFoundException extends NestedRuntimeException {

    public ProductOrderNotFoundException(String productCode) {
        super(String.format("Product with code %s not found", productCode));
    }
}

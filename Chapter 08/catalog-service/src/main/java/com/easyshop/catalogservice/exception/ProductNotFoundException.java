package com.easyshop.catalogservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class ProductNotFoundException extends NestedRuntimeException {

    public ProductNotFoundException(String productCode) {
        super(String.format("Product with code %s not found", productCode));
    }

}

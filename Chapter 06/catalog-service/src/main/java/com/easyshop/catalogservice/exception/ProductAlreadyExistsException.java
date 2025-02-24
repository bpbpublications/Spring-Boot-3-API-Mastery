package com.easyshop.catalogservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class ProductAlreadyExistsException extends NestedRuntimeException {

    public ProductAlreadyExistsException(String productCode) {
        super(String.format("Product with code %s already exists", productCode));
    }

}

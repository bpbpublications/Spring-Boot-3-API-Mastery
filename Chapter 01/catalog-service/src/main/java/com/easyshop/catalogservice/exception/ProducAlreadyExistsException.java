package com.easyshop.catalogservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class ProducAlreadyExistsException extends NestedRuntimeException {

    public ProducAlreadyExistsException(String productCode) {
        super(String.format("Product with code %s already exists", productCode));
    }

}

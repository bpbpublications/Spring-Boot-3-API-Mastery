package com.easyshop.graphqlservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class OrderNotFoundException extends NestedRuntimeException {

    public OrderNotFoundException(String productCode) {
        super(String.format("Order with code %s not found", productCode));
    }

}

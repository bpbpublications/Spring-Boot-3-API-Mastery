package com.easyshop.orderservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class OrderNotFoundException extends NestedRuntimeException {

    public OrderNotFoundException(String orderCode) {
        super(String.format("Order with code %s not found", orderCode));
    }

}

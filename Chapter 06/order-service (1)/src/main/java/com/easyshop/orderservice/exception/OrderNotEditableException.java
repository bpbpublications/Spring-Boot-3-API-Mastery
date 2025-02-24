package com.easyshop.orderservice.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class OrderNotEditableException extends NestedRuntimeException {

    public OrderNotEditableException(String productCode) {
        super(String.format("Order with code %s is not editable", productCode));
    }

}

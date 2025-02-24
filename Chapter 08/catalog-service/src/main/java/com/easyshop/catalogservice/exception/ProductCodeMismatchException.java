package com.easyshop.catalogservice.exception;

import org.springframework.core.NestedRuntimeException;

public class ProductCodeMismatchException extends NestedRuntimeException {

    public ProductCodeMismatchException() {
        super("Product code in the path is different from that in body");
    }
}

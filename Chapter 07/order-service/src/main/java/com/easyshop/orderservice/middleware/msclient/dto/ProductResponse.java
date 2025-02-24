package com.easyshop.orderservice.middleware.msclient.dto;

public record ProductResponse(
        String code,
        String category,
        Long price
) {}

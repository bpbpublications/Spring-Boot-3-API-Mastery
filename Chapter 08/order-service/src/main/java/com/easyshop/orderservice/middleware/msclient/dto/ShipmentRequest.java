package com.easyshop.orderservice.middleware.msclient.dto;

import lombok.Builder;

@Builder
public record ShipmentRequest(String orderCode) {
}

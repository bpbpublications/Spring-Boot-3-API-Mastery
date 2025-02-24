package com.easyshop.orderservice.middleware.queue.consumer.dto;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record ShipmentEvent(
        String trackingCode,
        String orderCode,
        String status,
        Instant shippingDate,
        Instant deliveryDate) {}

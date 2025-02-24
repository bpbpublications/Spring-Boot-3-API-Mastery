package com.easyshop.shippingservice.middleware.queue.producer.dto;

import lombok.Builder;

import java.time.Instant;

@Builder(toBuilder = true)
public record ShipmentEvent(
        String trackingCode,
        String orderCode,
        Instant shippingDate,
        Instant deliveryDate,
        String status) {}

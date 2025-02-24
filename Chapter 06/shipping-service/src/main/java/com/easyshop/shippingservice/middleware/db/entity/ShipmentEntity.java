package com.easyshop.shippingservice.middleware.db.entity;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Table("shipments")
@Builder(toBuilder = true)
public record ShipmentEntity(@Id Long shipmentId,
                             String trackingCode,
                             String orderCode,
                             String status,
                             Instant shippingDate,
                             Instant deliveryDate) {}
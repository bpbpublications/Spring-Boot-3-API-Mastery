package com.easyshop.orderservice.middleware.db.entity;

import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;

@Table("orders")
@Builder(toBuilder = true)
public record OrderEntity(
        @Id
        Long orderId,
        String orderCode,
        String status,
        @MappedCollection(idColumn = "order_id")
        Set<OrderItem> items,
        Long totalPrice,
        @CreatedDate
        Instant createdAt,
        @LastModifiedDate
        Instant updatedAt
) {}


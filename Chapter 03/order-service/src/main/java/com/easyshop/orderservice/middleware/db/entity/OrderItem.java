package com.easyshop.orderservice.middleware.db.entity;

import org.springframework.data.relational.core.mapping.Table;

@Table("order_items")
public record OrderItem(
        String productCode,
        int quantity
) {}

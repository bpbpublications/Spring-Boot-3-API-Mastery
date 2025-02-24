package com.easyshop.catalogservice.middleware.db.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "products")
public record ProductEntity(
        @Id Long productId,
        String code,
        String name,
        Long price,
        String brand,
        CategoryEntity category
) {}

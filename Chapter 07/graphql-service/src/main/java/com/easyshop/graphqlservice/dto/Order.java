package com.easyshop.graphqlservice.dto;

import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.List;

@Builder(toBuilder = true)
public record Order(String orderCode, String status, Long totalPrice,
                    OffsetDateTime createdAt, OffsetDateTime updatedAt,
                    List<Product> products) {}

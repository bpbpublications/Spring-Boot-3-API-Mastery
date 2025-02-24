package com.easyshop.graphqlservice.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record Product(String productCode, String name, String category,
                      Long price, String brand, Integer quantity) {}

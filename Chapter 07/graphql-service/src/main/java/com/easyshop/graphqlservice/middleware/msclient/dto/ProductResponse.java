package com.easyshop.graphqlservice.middleware.msclient.dto;

public record ProductResponse(String code, String name, String category,
                              Long price, String brand) {
}

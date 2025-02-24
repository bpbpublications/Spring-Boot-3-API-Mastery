package com.easyshop.graphqlservice.middleware.msclient.dto;

import java.util.List;

public record ProductPage(List<ProductResponse> content) {
}

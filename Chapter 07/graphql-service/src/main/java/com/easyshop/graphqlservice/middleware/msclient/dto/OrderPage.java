package com.easyshop.graphqlservice.middleware.msclient.dto;

import com.easyshop.graphqlservice.dto.Order;
import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record OrderPage(List<Order> content) {
}

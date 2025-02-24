package com.easyshop.orderservice.mapper;

import com.easyshop.orderservice.generated.model.*;
import com.easyshop.orderservice.middleware.db.entity.OrderEntity;
import com.easyshop.orderservice.middleware.db.entity.OrderItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "orderCode",
            expression = "java(this.generateOrderCode())")
    @Mapping(target = "items", source = "orderRequest.products")
    OrderEntity toEntity(OrderRequest orderRequest, long totalPrice, OrderStatus status);

    @Mapping(target = "products", source = "entity.items")
    OrderResponse toResponse(OrderEntity entity);

    OrderPageItem toOrderPageItem(OrderEntity entity);

    Set<OrderItem> toOrderItem(List<ProductOrder> productOrders);


    default String generateOrderCode() {
        var randomString = RandomStringUtils.randomAlphanumeric(5).toUpperCase();
        return randomString + System.currentTimeMillis();
    }
}

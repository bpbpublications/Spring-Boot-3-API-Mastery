package com.easyshop.graphqlservice.api;

import com.easyshop.graphqlservice.dto.Order;
import com.easyshop.graphqlservice.dto.Product;
import com.easyshop.graphqlservice.service.OrderService;
import graphql.GraphQLContext;
import graphql.schema.DataFetchingFieldSelectionSet;
import graphql.schema.SelectedField;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @QueryMapping
    public Collection<Order> orders(@Argument String status, DataFetchingFieldSelectionSet env, GraphQLContext context) {
        context.put("env", env);
        log.info("Call orders with status: {}", status);
        return orderService.findOrders(status);
    }

    @QueryMapping
    public Order findOrderByCode(@Argument String orderCode, DataFetchingFieldSelectionSet env, GraphQLContext context) {
        context.put("env", env);
        log.info("Call findOrderByCode with orderCode: {}", orderCode);
        return orderService.findOrderByCode(orderCode);
    }

    @BatchMapping
    public Map<Order, List<Product>> products(@Argument List<Order> orders, GraphQLContext context) {
        final DataFetchingFieldSelectionSet env = context.get("env");
        var fetch = needFetch(env);
        return orderService.getProductsFromOrders(orders, fetch);
    }

    private boolean needFetch(DataFetchingFieldSelectionSet dataFetchingEnvironment) {
        var excludedFields = Set.of("products/productCode", "products/quantity");
        return dataFetchingEnvironment.getFields().stream()
                .map(SelectedField::getQualifiedName)
                .filter(name -> name.startsWith("products/"))
                .anyMatch(fieldName -> !excludedFields.contains(fieldName));
    }


}

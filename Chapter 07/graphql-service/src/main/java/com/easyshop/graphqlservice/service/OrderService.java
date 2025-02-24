package com.easyshop.graphqlservice.service;

import com.easyshop.graphqlservice.dto.Order;
import com.easyshop.graphqlservice.dto.Product;
import com.easyshop.graphqlservice.exception.OrderNotFoundException;
import com.easyshop.graphqlservice.middleware.msclient.OrderClient;
import com.easyshop.graphqlservice.middleware.msclient.ProductClient;
import com.easyshop.graphqlservice.middleware.msclient.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderClient orderClient;
    private final ProductClient productClient;


    public Order findOrderByCode(String orderCode) {
        try {
            log.info("HTTP fetching order with orderCode: {}", orderCode);
            return orderClient.findOrderByCode(orderCode);
        }
        catch (HttpClientErrorException.NotFound nfe) {
            throw new OrderNotFoundException(orderCode);
        }

    }

    public Collection<Order> findOrders(String status) {
        var orderPage = orderClient.findOrders(0, 50, status);
        return orderPage.content().parallelStream()
                .map(order -> findOrderByCode(order.orderCode()))
                .toList();

    }

    public Map<Order, List<Product>> getProductsFromOrders(List<Order> orders, boolean fetch) {
        if (fetch) {
            return fetchProducts(orders);
        }
        else {
            return orders.stream()
                    .collect(Collectors.toMap(
                            order -> order,
                            Order::products
                    ));
        }

    }

    private Map<Order, List<Product>> fetchProducts(List<Order> orders) {
        var productCodes = orders.stream()
                .flatMap(order -> order.products().parallelStream().map(Product::productCode))
                .collect(Collectors.toSet());

        log.info("HTTP fetching products with productCodes: {}", productCodes);
        var products = productClient.findProducts(0, productCodes.size(), productCodes).content();

        var mapProduct = products.stream()
                .collect(Collectors.toMap(ProductResponse::code, product -> product));

        return orders.stream()
                .collect(Collectors.toMap(
                        order -> order,
                        order -> order.products()
                                .parallelStream()
                                .map(product ->
                                        buildProduct(product, mapProduct.get(product.productCode()))
                                )
                                .toList()

                ));
    }

    private Product buildProduct(Product product, ProductResponse productResponse) {
        return  product.toBuilder()
                .name(productResponse.name())
                .brand(productResponse.brand())
                .category(productResponse.category())
                .price(productResponse.price())
                .build();
    }
}

package com.easyshop.graphqlservice.api;

import com.easyshop.graphqlservice.config.GraphQlConfig;
import com.easyshop.graphqlservice.dto.Order;
import com.easyshop.graphqlservice.dto.Product;
import com.easyshop.graphqlservice.exception.ErrorHandler;
import com.easyshop.graphqlservice.exception.OrderNotFoundException;
import com.easyshop.graphqlservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@GraphQlTest
@Import({GraphQlConfig.class, ErrorHandler.class})
class OrderControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private OrderService orderService;

    @Test
    void findOrderByCodeOkTest() {
        var productSummary = Product.builder()
                .productCode("01")
                .build();

        var productDetail = Product.builder()
                .productCode("01")
                .name("aProduct")
                .build();

        var order = Order.builder()
                .orderCode("0001")
                .status("DELIVERED")
                .products(List.of(
                        productSummary
                ))
                .build();

        var expectedOrder = order.toBuilder()
                .products(List.of(productDetail))
                .build();

        when(orderService.findOrderByCode("0001"))
                .thenReturn(order);
        when(orderService.getProductsFromOrders(List.of(order), true))
                .thenReturn(Map.of(
                        order, List.of(productDetail)
                ));
        graphQlTester
                .documentName("orderQueries")
                .operationName("orderDetails")
                .variable("id", "0001")
                .execute()
                .path("findOrderByCode")
                .entity(Order.class)
                .isEqualTo(expectedOrder);
    }

    @Test
    void findOrderByCodeKoTest() {
        var ex = new OrderNotFoundException("0001");
        when(orderService.findOrderByCode("0001"))
                .thenThrow(ex);
        graphQlTester
                .documentName("orderQueries")
                .operationName("orderDetails")
                .variable("id", "0001")
                .execute()
                .errors()
                .expect(actualError ->
                        actualError.getErrorType().equals(ErrorType.NOT_FOUND)
                                &&
                                actualError.getMessage().equals(ex.getMessage())
                );
    }
}

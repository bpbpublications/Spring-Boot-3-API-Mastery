package com.easyshop.orderservice.api;

import com.easyshop.orderservice.exception.OrderNotFoundException;
import com.easyshop.orderservice.generated.model.OrderRequest;
import com.easyshop.orderservice.generated.model.OrderResponse;
import com.easyshop.orderservice.generated.model.ProductOrder;
import com.easyshop.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @MockBean
    private OrderService orderService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void addOrderOkTest() {
        var request = new OrderRequest()
                .products(List.of(
                        new ProductOrder("prod-code", 1)
                ));
        when(orderService.addOrder(request)).thenReturn("code1");
        webTestClient.post()
                .uri("/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Void.class)
                .consumeWith(result ->
                        assertThat(result.getResponseHeaders()
                                .get("Location").get(0))
                        .isEqualTo("http://localhost/orders/code1"));

    }

    @Test
    void addOrderKoValidationTest() {
        var request = new OrderRequest()
                .addProductsItem(new ProductOrder());
        webTestClient.post()
                .uri("/orders")
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void findOrderByCodeOkTest() {
        var responseExpected = new OrderResponse()
                .orderCode("code1")
                .addProductsItem(new ProductOrder().productCode("prod-code").quantity(1))
                .totalPrice(90000L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now());
        when(orderService.findOrderByCode("code1")).thenReturn(responseExpected);

        webTestClient.get()
                .uri("/orders/code1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponse.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).isEqualTo(responseExpected));

    }

    @Test
    void findOrderByCodeKoOrderNotFoundTest() {
        when(orderService.findOrderByCode("code1")).thenThrow(new OrderNotFoundException("code1"));

        webTestClient.get()
                .uri("/orders/code1")
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void editOrderByCodeOkTest() {
        var code = "code1";
        var request = new OrderRequest()
                .products(List.of(
                        new ProductOrder("code-product", 1)
                ));
        var responseExpected = new OrderResponse()
                .orderCode(code)
                .addProductsItem(new ProductOrder().productCode("code-product").quantity(1))
                .totalPrice(90000L)
                .createdAt(Instant.now())
                .updatedAt(Instant.now());

        when(orderService.editOrderByCode(code, request)).thenReturn(responseExpected);
        webTestClient.put()
                .uri("/orders/code1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OrderResponse.class)
                .consumeWith(result -> assertThat(result.getResponseBody()).isEqualTo(responseExpected));

    }

    @Test
    void editOrderByCodeKoOrderNotFoundTest() {
        var request = new OrderRequest()
                .products(List.of(
                        new ProductOrder("code-product", 1)
                ));
        when(orderService.editOrderByCode("code1", request)).thenThrow(new OrderNotFoundException("code1"));

        webTestClient.put()
                .uri("/orders/code1")
                .bodyValue(request)
                .exchange()
                .expectStatus().isNotFound();

    }

    @Test
    void removeOrderByCodeOkTest() {
        doNothing().when(orderService).removeOrderByCode("code1");

        webTestClient.delete()
                .uri("/orders/code1")
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).removeOrderByCode("code1");

    }

    @Test
    void removeOrderByCodeKoOrderNotFoundTest() {
        doThrow(new OrderNotFoundException("code1")).when(orderService).removeOrderByCode("code1");

        webTestClient.delete()
                .uri("/orders/code1")
                .exchange()
                .expectStatus().isNotFound();

        verify(orderService).removeOrderByCode("code1");

    }

}

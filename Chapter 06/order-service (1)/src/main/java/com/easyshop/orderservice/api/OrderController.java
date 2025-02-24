package com.easyshop.orderservice.api;

import com.easyshop.orderservice.generated.api.OrderApi;
import com.easyshop.orderservice.generated.model.OrderPage;
import com.easyshop.orderservice.generated.model.OrderRequest;
import com.easyshop.orderservice.generated.model.OrderResponse;
import com.easyshop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi {

    private final OrderService orderService;

    @Override
    public ResponseEntity<Void> addOrder(OrderRequest orderRequest) {
        final String orderCode = orderService.addOrder(orderRequest);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString() + "/" + orderCode;
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @Override
    public ResponseEntity<OrderResponse> findOrderByCode(String orderCode) {
        var orderResponse = orderService.findOrderByCode(orderCode);
        return ResponseEntity.ok(orderResponse);
    }

    @Override
    public ResponseEntity<OrderPage> findOrders(Integer pageNumber, Integer pageSize, List<String> sort, String order, String status) {
        return ResponseEntity.ok(orderService.findAll(pageNumber, pageSize, status, sort, order));
    }

    @Override
    public ResponseEntity<OrderResponse> editOrderByCode(String orderCode, OrderRequest orderRequest) {
        return ResponseEntity.ok(orderService.editOrderByCode(orderCode, orderRequest));
    }

    @Override
    public ResponseEntity<Void> removeOrderByCode(String orderCode) {
        orderService.removeOrderByCode(orderCode);
        return ResponseEntity.noContent().build();
    }
}

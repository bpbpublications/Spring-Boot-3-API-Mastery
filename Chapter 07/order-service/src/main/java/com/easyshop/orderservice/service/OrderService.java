package com.easyshop.orderservice.service;

import com.easyshop.orderservice.exception.OrderNotEditableException;
import com.easyshop.orderservice.exception.OrderNotFoundException;
import com.easyshop.orderservice.generated.model.*;
import com.easyshop.orderservice.mapper.OrderMapper;
import com.easyshop.orderservice.middleware.db.entity.OrderEntity;
import com.easyshop.orderservice.middleware.db.repo.OrderRepository;
import com.easyshop.orderservice.middleware.msclient.ProductClient;
import com.easyshop.orderservice.middleware.msclient.ShipmentClient;
import com.easyshop.orderservice.middleware.msclient.dto.ShipmentRequest;
import com.easyshop.orderservice.middleware.queue.consumer.dto.ShipmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.easyshop.orderservice.generated.model.OrderStatus.*;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.startsWith;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {

    private final Map<String, String> statusMap = Map.of(
            TAKEN_CHARGE.getValue(), DELIVERING.getValue(),
            DELIVERING.getValue(), DELIVERED.getValue()
    );

    private final OrderRepository orderRepository;
    private final ProductClient productClient;
    private final OrderMapper orderMapper;
    private final ShipmentClient shipmentClient;

    @Transactional
    public String addOrder(OrderRequest orderRequest) {
        var totalPrice = calculateTotalPrice(orderRequest);
        var entity = orderMapper.toEntity(orderRequest, totalPrice,
                OrderStatus.TAKEN_CHARGE);
        orderRepository.save(entity);
        shipmentClient.addShipment(ShipmentRequest.builder()
                .orderCode(entity.orderCode())
                .build());
        return entity.orderCode();
    }

    private Long calculateTotalPrice(OrderRequest orderRequest) {
        return orderRequest.getProducts().stream()
                .map(this::createQuantityCodePair)
                .map(quantityAndPrice -> quantityAndPrice.getFirst() * quantityAndPrice.getSecond())
                .reduce(0L, Long::sum);
    }

    private Pair<Integer, Long> createQuantityCodePair(ProductOrder productOrder) {
        var response = productClient.findByProductCode(productOrder.getProductCode());
        return Pair.of(productOrder.getQuantity(), response.price());
    }

    public OrderResponse findOrderByCode(String orderCode) {
        var optionalEntity = orderRepository.findByOrderCode(orderCode);
        if(optionalEntity.isPresent()) {
            return orderMapper.toResponse(optionalEntity.get());
        }
        else {
            throw new OrderNotFoundException(orderCode);
        }
    }

    public OrderPage findAll(int pageNumber, int pageSize, String status, List<String> sort, String order) {
        var pageRequest = createPageRequest(pageNumber, pageSize, sort, order);
        var example = createExample(status);
        final Page<OrderPageItem> page = orderRepository.findAll(example, pageRequest)
                .map(orderMapper::toOrderPageItem);
        return new OrderPage()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .number(page.getNumber())
                .number(page.getNumberOfElements());
    }

    private Pageable createPageRequest(int pageNumber, int pageSize, List<String> sort, String order) {
        if(sort != null) {
            Sort sortInput = Sort.by(Sort.Direction.valueOf(order), sort.toArray(new String[]{}));
            return PageRequest.of(pageNumber, pageSize, sortInput);
        }
        return PageRequest.of(pageNumber, pageSize);
    }

    private Example<OrderEntity> createExample(String status) {
        var probe = OrderEntity.builder()
                .status(status)
                .build();

        var matcher = ExampleMatcher.matching()
                .withMatcher("status", startsWith());

        return Example.of(probe, matcher);
    }

    @Transactional
    public OrderResponse editOrderByCode(String orderCode, OrderRequest orderRequest) {
        var optionalOrder = orderRepository.findByOrderCode(orderCode);
        if(optionalOrder.isPresent()) {
            var order = optionalOrder.get();
            if(TAKEN_CHARGE.getValue().equals(order.status())) {
                var totalPrice = calculateTotalPrice(orderRequest);
                var orderItems = orderMapper.toOrderItem(orderRequest.getProducts());
                var updatedOrder = order.toBuilder().items(orderItems).totalPrice(totalPrice).build();
                updatedOrder = orderRepository.save(updatedOrder);
                return orderMapper.toResponse(updatedOrder);
            }
            else {
                throw new OrderNotEditableException(orderCode);
            }
        }
        else {
            throw new OrderNotFoundException(orderCode);
        }
    }

    @Transactional
    public void removeOrderByCode(String orderCode) {
        var optionalOrder = orderRepository.findByOrderCode(orderCode);
        if(optionalOrder.isPresent()) {
            var order = optionalOrder.get();
            if(TAKEN_CHARGE.getValue().equals(order.status())) {
                orderRepository.delete(optionalOrder.get());
            }
            else {
                throw new OrderNotEditableException(orderCode);
            }
        }
        else {
            throw new OrderNotFoundException(orderCode);
        }
    }

    @Transactional
    public void updateOrderByShipment(ShipmentEvent shipmentEvent) {
        orderRepository.findByOrderCode(shipmentEvent.orderCode())
                .map(order -> {
                    if (isAConsistentStatus(order, shipmentEvent.status())) {
                        var updatedEntity = order.toBuilder()
                                .status(shipmentEvent.status())
                                .build();
                        return orderRepository.save(updatedEntity);
                    }
                    return order;
                })
                .orElseThrow(() ->
                        new OrderNotFoundException(shipmentEvent.orderCode()));
    }

    private boolean isAConsistentStatus(OrderEntity order, String newStatus) {
        return StringUtils.hasText(newStatus) &&
                newStatus.equals(statusMap.get(order.status()));
    }

}

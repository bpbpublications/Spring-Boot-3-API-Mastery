package com.easyshop.orderservice.middleware.queue.consumer;

import com.easyshop.orderservice.generated.model.OrderStatus;
import com.easyshop.orderservice.middleware.db.entity.OrderEntity;
import com.easyshop.orderservice.middleware.db.repo.OrderRepository;
import com.easyshop.orderservice.middleware.queue.consumer.dto.ShipmentEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@ActiveProfiles("test")
@Import(TestChannelBinderConfiguration.class)
class ShipmentEventFunctionsTest {

    @Autowired
    private InputDestination input;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private Consumer<ErrorMessage> handleShipmentEventError;


    @Test
    void testReceivedMessageOk() throws IOException {
        var event = ShipmentEvent.builder()
                .orderCode("001")
                .status(OrderStatus.DELIVERED.getValue())
                .build();

        var message = MessageBuilder
                .withPayload(objectMapper.writeValueAsBytes(event))
                .setHeader(KafkaHeaders.KEY, "001")
                .build();

        var entityBefore = OrderEntity.builder().orderCode("001")
                .status(OrderStatus.DELIVERING.getValue()).build();
        var entityAfter = OrderEntity.builder().orderCode("001")
                .status(OrderStatus.DELIVERED.getValue()).build();

        when(orderRepository.findByOrderCode("001"))
                .thenReturn(Optional.of(entityBefore));
        when(orderRepository.save(entityAfter))
                .thenReturn(entityAfter);

        this.input.send(message, "shipment_event_topic");

        verify(orderRepository).findByOrderCode("001");
        verify(orderRepository).save(entityAfter);
    }

    @Test
    void testReceivedMessageKo() throws IOException {
        var event = ShipmentEvent.builder()
                .orderCode("not-exists")
                .status(OrderStatus.DELIVERED.getValue())
                .build();

        var message = MessageBuilder
                .withPayload(objectMapper.writeValueAsBytes(event))
                .setHeader(KafkaHeaders.KEY, "not-exists")
                .build();


        when(orderRepository.findByOrderCode("not-exists"))
                .thenReturn(Optional.empty());

        this.input.send(message, "shipment_event_topic");

        verify(orderRepository, times(3)).findByOrderCode("not-exists");
        verify(orderRepository, never()).save(any());
        verify(handleShipmentEventError).accept(any(ErrorMessage.class));
    }
}

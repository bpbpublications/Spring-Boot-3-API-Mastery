package com.easyshop.orderservice.middleware.queue.consumer;

import com.easyshop.orderservice.IntegrationEnvConfig;
import com.easyshop.orderservice.generated.model.OrderStatus;
import com.easyshop.orderservice.middleware.db.entity.OrderEntity;
import com.easyshop.orderservice.middleware.db.repo.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@Testcontainers
@Import(IntegrationEnvConfig.class)
@ActiveProfiles("test-it")
class ShipmentEventFunctionsTestIT {


    @MockBean
    private OrderRepository orderRepository;

    @Autowired
    private StreamBridge streamBridge;



    @Test
    void testShipmentEventFunctions() {

        var eventJson = """
                {
                "orderCode": "001",
                "status": "DELIVERED"
                }
                """;

        var entityBefore = OrderEntity.builder().orderCode("001")
                .status(OrderStatus.DELIVERING.getValue()).build();
        var entityAfter = OrderEntity.builder().orderCode("001")
                .status(OrderStatus.DELIVERED.getValue()).build();

        when(orderRepository.findByOrderCode("001"))
                .thenReturn(Optional.of(entityBefore));
        when(orderRepository.save(entityAfter))
                .thenReturn(entityAfter);


        var kafkaMessage = MessageBuilder
                .withPayload(eventJson)
                .setHeader(KafkaHeaders.KEY,"001".getBytes())
                .build();

        streamBridge.send("shipment_event_topic", kafkaMessage);

        verify(orderRepository, timeout(2000L)).findByOrderCode("001");
        verify(orderRepository, timeout(2000L)).save(entityAfter);
    }
}

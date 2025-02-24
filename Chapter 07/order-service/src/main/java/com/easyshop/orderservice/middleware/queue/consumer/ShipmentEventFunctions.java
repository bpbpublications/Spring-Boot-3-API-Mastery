package com.easyshop.orderservice.middleware.queue.consumer;

import com.easyshop.orderservice.middleware.queue.consumer.dto.ShipmentEvent;
import com.easyshop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.ErrorMessage;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class ShipmentEventFunctions {

    private final OrderService orderService;


    @Bean
    Consumer<Message<ShipmentEvent>> handleShipmentEvent() {
        return message -> {
            var shipmentEvent = message.getPayload();
            log.info("received shipment event: {}", message);
            orderService.updateOrderByShipment(shipmentEvent);
        };
    }

    @Bean
    Consumer<ErrorMessage> handleShipmentEventError() {
        return message -> {
            Throwable error = message.getPayload();
            Message<?> originalMessage = message.getOriginalMessage();
            MessageHeaders headers = originalMessage.getHeaders();
            byte[] originalMessageKey = (byte[]) headers.get(KafkaHeaders.RECEIVED_KEY);
            byte[] originalMessagePayload = (byte[]) originalMessage.getPayload();
            log.info("Original MessageKey: {}, MessagePayload: {}, with error: {}",
                    new String(originalMessageKey, StandardCharsets.UTF_8),
                    new String(originalMessagePayload, StandardCharsets.UTF_8),
                    error.getCause().getMessage());
            //Save to db or do something else....
        };
    }
}

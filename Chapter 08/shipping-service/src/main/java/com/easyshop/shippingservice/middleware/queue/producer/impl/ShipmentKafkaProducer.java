package com.easyshop.shippingservice.middleware.queue.producer.impl;

import com.easyshop.shippingservice.middleware.queue.producer.ShipmentProducer;
import com.easyshop.shippingservice.middleware.queue.producer.dto.ShipmentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipmentKafkaProducer implements ShipmentProducer {

    private final StreamBridge streamBridge;

    @Override
    public boolean publish(ShipmentEvent message) {
        log.info("Sending message: {}", message);
        var kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.KEY, message.orderCode().getBytes())
                .build();
        return streamBridge.send("shipmentEventProducer-out-0", kafkaMessage);
    }
}

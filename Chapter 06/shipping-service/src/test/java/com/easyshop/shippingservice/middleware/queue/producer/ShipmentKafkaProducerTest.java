package com.easyshop.shippingservice.middleware.queue.producer;

import com.easyshop.shippingservice.middleware.queue.producer.dto.ShipmentEvent;
import com.easyshop.shippingservice.service.ShipmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jdbc.JdbcRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, JdbcRepositoriesAutoConfiguration.class})
@Import(TestChannelBinderConfiguration.class)
class ShipmentKafkaProducerTest {


    @Autowired
    private OutputDestination outputDestination;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShipmentProducer shipmentProducer;

    @MockBean
    private ShipmentService shipmentService;

    @Test
    void publishOk() throws IOException {
        var event = ShipmentEvent.builder()
                .orderCode("001")
                .status("DELIVERED")
                .build();

        shipmentProducer.publish(event);


        byte[] payload = outputDestination.receive(2000L, "shipment_event_topic").getPayload();
        ShipmentEvent shipmentEvent = objectMapper.readValue(payload, ShipmentEvent.class);

        assertThat(shipmentEvent).isEqualTo(event);
    }
}

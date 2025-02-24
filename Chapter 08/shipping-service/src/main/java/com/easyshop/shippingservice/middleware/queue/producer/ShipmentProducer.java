package com.easyshop.shippingservice.middleware.queue.producer;

import com.easyshop.shippingservice.middleware.queue.producer.dto.ShipmentEvent;

public interface ShipmentProducer {
    boolean publish(ShipmentEvent message);
}

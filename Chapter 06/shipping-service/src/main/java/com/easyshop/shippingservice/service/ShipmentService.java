package com.easyshop.shippingservice.service;

import com.easyshop.shippingservice.exeption.ShipmentNotFoundException;
import com.easyshop.shippingservice.generated.model.ShipmentRequest;
import com.easyshop.shippingservice.generated.model.ShipmentResponse;
import com.easyshop.shippingservice.generated.model.ShipmentStatus;
import com.easyshop.shippingservice.mapper.ShipmentMapper;
import com.easyshop.shippingservice.middleware.db.entity.ShipmentEntity;
import com.easyshop.shippingservice.middleware.db.repo.ShipmentRepository;
import com.easyshop.shippingservice.middleware.queue.producer.ShipmentProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.*;

import static com.easyshop.shippingservice.generated.model.ShipmentStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;
    private final ShipmentProducer shipmentProducer;
    private final ShipmentMapper shipmentMapper;

    @Transactional
    public String addShipment(ShipmentRequest request) {
        var entity = ShipmentEntity.builder()
                .orderCode(request.getOrderCode())
                .trackingCode(UUID.nameUUIDFromBytes(request.getOrderCode().getBytes()).toString())
                .status(TAKING_CHARGE.getValue())
                .build();

        var saved = shipmentRepository.save(entity);
        sendShippingEvents(saved);
        return saved.trackingCode();
    }

    private void sendShippingEvents(ShipmentEntity entity) {
        var shipmentDate = Instant.now();
        var deliveryDate = shipmentDate.plusSeconds(10);

        var shipStatusFuture = sendEvent(entity, DELIVERING, shipmentDate, deliveryDate, 5L);
        var deliveryStatusFuture = sendEvent(entity, DELIVERED, shipmentDate, deliveryDate, 15L);

        CompletableFuture.allOf(shipStatusFuture, deliveryStatusFuture);
    }

    private CompletableFuture<Void> sendEvent(ShipmentEntity entity, ShipmentStatus status, Instant shipmentDate, Instant deliveryDate, long delaySeconds) {
        return CompletableFuture.runAsync(() -> {

            var updatedEntity = entity.toBuilder()
                    .status(status.getValue())
                    .shippingDate(shipmentDate)
                    .deliveryDate(deliveryDate)
                    .build();

            shipmentRepository.save(updatedEntity);
            var message = shipmentMapper.toMessage(updatedEntity);
            shipmentProducer.publish(message);
        }, CompletableFuture.delayedExecutor(delaySeconds, TimeUnit.SECONDS));
    }

    public ShipmentResponse findShipmentByTrackingCode(String trackingCode) {
        var optionEntity = shipmentRepository.findByTrackingCode(trackingCode);
        if(optionEntity.isPresent()) {
            var entity = optionEntity.get();
            return new ShipmentResponse()
                    .trackingCode(entity.trackingCode())
                    .orderCode(entity.orderCode())
                    .shippingDate(entity.shippingDate())
                    .deliveryDate(entity.deliveryDate());
        }

        else {
            throw new ShipmentNotFoundException(trackingCode);
        }
    }
}

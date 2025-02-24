package com.easyshop.shippingservice.middleware.db.repo;

import com.easyshop.shippingservice.middleware.db.entity.ShipmentEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShipmentRepository extends CrudRepository<ShipmentEntity, Long> {

    Optional<ShipmentEntity> findByTrackingCode(String trackingCode);
}

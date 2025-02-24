package com.easyshop.shippingservice.mapper;

import com.easyshop.shippingservice.middleware.db.entity.ShipmentEntity;
import com.easyshop.shippingservice.middleware.queue.producer.dto.ShipmentEvent;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShipmentMapper {

    ShipmentEvent toMessage(ShipmentEntity entity);
}

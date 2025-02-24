package com.easyshop.shippingservice.exeption;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class ShipmentNotFoundException extends NestedRuntimeException {

    public ShipmentNotFoundException(String trackingCode) {
        super(String.format("Shipment with tracking code %s not found", trackingCode));
    }

}

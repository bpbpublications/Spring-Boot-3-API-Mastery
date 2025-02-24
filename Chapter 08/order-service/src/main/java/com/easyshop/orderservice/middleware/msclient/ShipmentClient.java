package com.easyshop.orderservice.middleware.msclient;

import com.easyshop.orderservice.middleware.msclient.dto.ShipmentRequest;

public interface ShipmentClient {

    void addShipment(ShipmentRequest request);
}

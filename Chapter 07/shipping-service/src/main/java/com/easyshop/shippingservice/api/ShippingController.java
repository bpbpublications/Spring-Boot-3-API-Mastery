package com.easyshop.shippingservice.api;

import com.easyshop.shippingservice.generated.api.ShipmentApi;
import com.easyshop.shippingservice.generated.model.ShipmentRequest;
import com.easyshop.shippingservice.generated.model.ShipmentResponse;
import com.easyshop.shippingservice.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ShippingController implements ShipmentApi {

    private final ShipmentService shippingService;

    @Override
    public ResponseEntity<Void> addShipment(ShipmentRequest shipmentRequest) {
        var trackingCode = shippingService.addShipment(shipmentRequest);
        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .toUriString() + "/" + trackingCode;
        return ResponseEntity.created(URI.create(uri)).build();
    }

    @Override
    public ResponseEntity<ShipmentResponse> findShipmentByTrackingCode(String trackingCode) {
        var response = shippingService.findShipmentByTrackingCode(trackingCode);
        return ResponseEntity.ok(response);
    }
}

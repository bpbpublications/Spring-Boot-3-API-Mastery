package com.easyshop.edgeservice.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(("catalog-service-fallback"))
public class FallbackController {

    @GetMapping
    public Mono<ResponseEntity<Object>> fallbackCatalogServiceGet() {
        return Mono.just(ResponseEntity.noContent().build());
    }

    @PostMapping
    @PutMapping
    @DeleteMapping
    public Mono<ResponseEntity<Object>> fallbackCatalogServiceNotGet() {
        return Mono.just(ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build());
    }
}

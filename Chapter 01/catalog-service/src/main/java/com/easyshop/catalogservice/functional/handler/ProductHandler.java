package com.easyshop.catalogservice.functional.handler;

import com.easyshop.catalogservice.exception.ProducAlreadyExistsException;
import com.easyshop.catalogservice.generated.model.ProductRequest;
import com.easyshop.catalogservice.mapper.ProductMapper;
import com.easyshop.catalogservice.middleware.db.entity.ProductEntity;
import com.easyshop.catalogservice.middleware.db.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductHandler {

    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public Mono<ServerResponse> addProduct(ServerRequest request) {
        return request.bodyToMono(ProductRequest.class)
                .flatMap(this::insertNewProduct)
                .map(entitySaved -> request.uri() + "/" + entitySaved.code())
                .flatMap(uriString -> ServerResponse.created(URI.create(uriString)).build());
    }

    private Mono<ProductEntity> insertNewProduct(ProductRequest productRequest) {
        final ProductEntity productEntity = productMapper.toEntity(productRequest);
        return productRepository.save(productEntity)
                .doOnNext(entitySaved -> log.debug("Product saved: {}", entitySaved))
                .onErrorResume(DuplicateKeyException.class, e ->
                        Mono.error(new ProducAlreadyExistsException(productEntity.code())));
    }
}

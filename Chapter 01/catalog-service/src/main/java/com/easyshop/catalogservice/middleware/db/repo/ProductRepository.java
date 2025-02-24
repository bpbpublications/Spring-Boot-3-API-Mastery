package com.easyshop.catalogservice.middleware.db.repo;

import com.easyshop.catalogservice.middleware.db.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository extends R2dbcRepository<ProductEntity, Long> {

    Mono<ProductEntity> findByCode(String code);

    Flux<ProductEntity> findAllBy(Pageable pageable);
}

package com.easyshop.catalogservice.service;

import com.easyshop.catalogservice.exception.ProducAlreadyExistsException;
import com.easyshop.catalogservice.exception.ProductNotFoundException;
import com.easyshop.catalogservice.exception.ProductCodeMismatchException;
import com.easyshop.catalogservice.generated.model.ProductRequest;
import com.easyshop.catalogservice.generated.model.ProductResponse;
import com.easyshop.catalogservice.mapper.ProductMapper;
import com.easyshop.catalogservice.middleware.db.entity.ProductEntity;
import com.easyshop.catalogservice.middleware.db.repo.ProductRepository;
import com.easyshop.catalogservice.model.ProductPageResponse;
import com.easyshop.catalogservice.model.PutProduct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public Mono<ProductResponse> findProductByCode(String productCode) {
        return productRepository.findByCode(productCode)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productCode)))
                .map(productMapper::toProductResponse);
    }

    public Mono<ProductPageResponse> findAll(int pageNumber, int size) {
        log.info("pageNumber: {}, size: {}", pageNumber, size);
        final Pageable pageable = PageRequest.of(pageNumber, size);
        return productRepository.findAllBy(pageable)
                .collectList()
                .doOnNext(productEntities -> log.info("retrieved products: {}", productEntities))
                .map(productMapper::toProductResponse)
                .zipWhen(productResponses -> productRepository.count())
                .map(productResponsesWithCount -> new ProductPageResponse(productResponsesWithCount.getT1(), pageable, productResponsesWithCount.getT2()));
    }

    @Transactional
    public Mono<String> addProduct(ProductRequest productRequest) {
        return insertNewProduct(productRequest)
                .map(ProductEntity::code);
    }

    private Mono<ProductEntity> insertNewProduct(ProductRequest productRequest) {
        final ProductEntity productEntity = productMapper.toEntity(productRequest);
        return productRepository.save(productEntity)
                        .doOnNext(entitySaved -> log.debug("Product saved: {}", entitySaved))
                        .onErrorResume(DuplicateKeyException.class, e ->
                                Mono.error(new ProducAlreadyExistsException(productEntity.code())));
    }

    @Transactional
    public Mono<PutProduct> editProduct(String productCode, ProductRequest productRequest) {
        if(productCode.equals(productRequest.getCode())) {
            return Mono.error(new ProductCodeMismatchException());
        }

        return productRepository.findByCode(productCode)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(productCode)))
                .map(productEntity -> productMapper.toEntity(productRequest, productEntity.productId()))
                .flatMap(productRepository::save)
                .map(productMapper::toProductResponse)
                .map(productResponse -> new PutProduct(productResponse, false))
                .onErrorResume(ProductNotFoundException.class, e ->
                        insertNewProduct(productRequest)
                                .map(code -> new PutProduct(null, true)));
    }

    @Transactional
    public Mono<Void> deleteProduct(String productCode) {
        return productRepository.findByCode(productCode)
                .flatMap(productEntity -> productRepository.deleteById(productEntity.productId()));
    }

}

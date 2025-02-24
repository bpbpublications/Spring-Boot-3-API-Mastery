package com.easyshop.catalogservice.api;

import com.easyshop.catalogservice.generated.api.ProductApi;
import com.easyshop.catalogservice.generated.model.ProductRequest;
import com.easyshop.catalogservice.generated.model.ProductResponse;
import com.easyshop.catalogservice.model.ProductPageResponse;
import com.easyshop.catalogservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController implements ProductApi {

    private final ProductService productService;

    @Override
    public Mono<ResponseEntity<ProductPageResponse>> findProducts(Integer page, Integer size, List<String> productCode, final ServerWebExchange exchange) {
        return productService.findAll(page, size, productCode)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> findProductByCode(String productCode, ServerWebExchange exchange) {
        return productService.findProductByCode(productCode)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Void>> addProduct(Mono<ProductRequest> productRequest, ServerWebExchange exchange) {
       return productRequest
               .flatMap(productService::addProduct)
               .map(productCode -> ResponseEntity
                       .created(URI.create(exchange.getRequest().getURI() + "/" + productCode)).build());
    }

    @Override
    public Mono<ResponseEntity<ProductResponse>> editProduct(String productCode, Mono<ProductRequest> productRequest,  final ServerWebExchange exchange) {
        return productRequest
                .flatMap(request -> productService.editProduct(productCode, request))
                .map(putProduct -> {
                    if(putProduct.newProduct()) {
                        return ResponseEntity.created(exchange.getRequest().getURI()).build();
                    }
                    return ResponseEntity.ok(putProduct.productResponse());
                });
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteProduct(String productCode, ServerWebExchange exchange) {
        return productService.deleteProduct(productCode)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}

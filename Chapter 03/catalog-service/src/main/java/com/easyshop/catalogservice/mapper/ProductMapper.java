package com.easyshop.catalogservice.mapper;

import com.easyshop.catalogservice.generated.model.ProductRequest;
import com.easyshop.catalogservice.generated.model.ProductResponse;
import com.easyshop.catalogservice.middleware.db.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductRequest productRequest);

    ProductEntity toEntity(ProductRequest productRequest, Long productId);

    ProductResponse toProductResponse(ProductEntity productEntity);

    List<ProductResponse> toProductResponse(List<ProductEntity> productEntities);

}

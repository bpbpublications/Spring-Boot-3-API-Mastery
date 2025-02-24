package com.easyshop.catalogservice.model;

import com.easyshop.catalogservice.generated.model.ProductResponse;

public record PutProduct(ProductResponse productResponse,
                         boolean newProduct) {}

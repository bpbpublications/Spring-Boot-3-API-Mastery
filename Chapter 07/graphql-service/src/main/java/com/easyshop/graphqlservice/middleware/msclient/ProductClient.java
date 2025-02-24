package com.easyshop.graphqlservice.middleware.msclient;

import com.easyshop.graphqlservice.dto.Product;
import com.easyshop.graphqlservice.middleware.msclient.dto.ProductPage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.Collection;

public interface ProductClient {

    @GetExchange("/{productCode}")
    Product findProductByCode(@PathVariable String productCode);

    @GetExchange
    ProductPage findProducts(@RequestParam Integer page, @RequestParam Integer size,
                             @RequestParam Collection<String> productCode);
}

package com.easyshop.catalogservice.functional.router;

import com.easyshop.catalogservice.functional.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class ProductRouter {

    @Bean
    public RouterFunction<ServerResponse> addProductRouter(ProductHandler productHandler) {
        return route(POST("/products/v2")
                .and(accept(MediaType.APPLICATION_JSON)), productHandler::addProduct);
    }
}

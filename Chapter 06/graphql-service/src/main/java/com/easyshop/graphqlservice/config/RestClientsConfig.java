package com.easyshop.graphqlservice.config;

import com.easyshop.graphqlservice.middleware.msclient.OrderClient;
import com.easyshop.graphqlservice.middleware.msclient.ProductClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientsConfig {

    @Bean
    OrderClient orderClientNew(RestClient.Builder builder,
                               GraphQlServiceProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.getOrderserviceUrl())
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(OrderClient.class);
    }

    @Bean
    ProductClient productClientNew(RestClient.Builder builder,
                                   GraphQlServiceProperties properties) {
        RestClient restClient = builder
                .baseUrl(properties.getCatalogserviceUrl())
                .build();

        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ProductClient.class);
    }
}

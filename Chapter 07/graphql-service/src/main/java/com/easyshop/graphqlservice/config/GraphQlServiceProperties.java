package com.easyshop.graphqlservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "graphql.service")
@Data
public class GraphQlServiceProperties {

    private String catalogserviceUrl;
    private String orderserviceUrl;
}

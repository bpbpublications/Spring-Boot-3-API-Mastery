package com.easyshop.orderservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class IntegrationEnvConfig {


    static KafkaContainer kafka = new KafkaContainer(DockerImageName
            .parse("confluentinc/cp-kafka:7.6.1"))
            .withKraft();

    static PostgreSQLContainer<?> pg = new PostgreSQLContainer<>(DockerImageName
            .parse("postgres:16.2"))
            .withDatabaseName("easyshopdb_order")
            .withUsername("user")
            .withPassword("password");


    static {
        pg.start();
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        System.setProperty("spring.cloud.stream.kafka.binder.brokers", kafka.getBootstrapServers());
        System.setProperty("spring.datasource.url", pg.getJdbcUrl());
    }
}

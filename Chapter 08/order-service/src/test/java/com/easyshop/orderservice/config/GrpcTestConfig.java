package com.easyshop.orderservice.config;

import com.easyshop.catalogservice.proto.ProductProtoServiceGrpc;
import com.easyshop.orderservice.middleware.msclient.ProductClient;
import com.easyshop.orderservice.middleware.msclient.impl.GrpcProductClient;
import net.devh.boot.grpc.client.autoconfigure.GrpcClientAutoConfiguration;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerAutoConfiguration;
import net.devh.boot.grpc.server.autoconfigure.GrpcServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ImportAutoConfiguration({
        GrpcServerAutoConfiguration.class, // Create required server beans
        GrpcServerFactoryAutoConfiguration.class, // Select server implementation
        GrpcClientAutoConfiguration.class}) // Support @GrpcClient annotation
public class GrpcTestConfig {

    @Bean
    ProductProtoServiceGrpcMock productProtoServiceGrpcMock() {
        return new ProductProtoServiceGrpcMock();
    }

    @Bean
    ProductClient productClient(@GrpcClient("productProtoService")
                                ProductProtoServiceGrpc.ProductProtoServiceBlockingStub productProtoService) {

        return new GrpcProductClient(productProtoService);
    }

}

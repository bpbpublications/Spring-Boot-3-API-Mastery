# Chapter 02

In this chapter we build a catalog service application with Spring WebFlux and the API-first approach. This microservice contains
CRUD endpoints for the "product" domain object.

## Initialize Catalog Service with Spring Initializr

Download the project structure from [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=21&groupId=com.easyshop&artifactId=catalog-service&name=catalog-service&description=Catalog%20Service%20microservice%20with%20Spring%20WebFlux&packageName=com.easyshop.catalogservice&dependencies=webflux,data-r2dbc,validation,postgresql,lombok,testcontainers) GUI.

Or you can use the REST API of Spring Initializr:

```bash
curl https://start.spring.io/starter.zip -d groupId=com.easyshop -d artifactId=catalog-service -d name=catalog-service -d packageName=com.easyshop.catalogservice -d dependencies=webflux,data-r2dbc,validation,postgresql,lombok,testcontainers -d javaVersion=21 -d bootVersion=3.2.4 -d type=maven-project -d description="Catalog Service microservice with Spring WebFlux" -o catalog-service.zip
```

## Run the project
- Start Docker
- In the chapter-02/infra/docker path, run `docker compose up -d`
- In the chapter-02/catalog-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)
# Chapter 03

In this chapter we build a order service application with Spring MVC with virtual threads and the API-first approach. This microservice contains
CRUD endpoints for the "order" domain object.

## Initialize Catalog Service with Spring Initializr

Download the project structure from [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=21&groupId=com.easyshop&artifactId=order-service&name=order-service&description=Order%20Service%20microservice%20with%20Spring%20MVC&packageName=com.easyshop.orderservice&dependencies=web,data-jdbc,validation,postgresql,lombok,testcontainers) GUI.

Or you can use the REST API of Spring Initializr:

```bash
curl https://start.spring.io/starter.zip -d groupId=com.easyshop -d artifactId=order-service -d name=order-service -d packageName=com.easyshop.orderservice -d dependencies=web,data-jdbc,validation,postgresql,lombok,testcontainers -d javaVersion=21 -d bootVersion=3.2.4 -d type=maven-project -d description="Order Service microservice with Spring MVC" -o order-service.zip
```
Create some products:
```bash
http :8080/products code=0001 name="ALaptop" category=laptop price=60000 brand=FirstBrand
http :8080/products code=0002 name="SuperLaptop" category=laptop price=30000 brand=FirstBrand
```

Create an order with these products with POST API:
```bash
http :8081/orders \
  Content-Type:application/json \
  "products[0][productCode]"=0001 \
  "products[0][quantity]":=2 \
  "products[1][productCode]"=0002 \
  "products[1][quantity]":=1
```

## Run the project
- Start Docker
- In the chapter-03/infra/docker path, run `docker compose up -d`
- In the chapter-03/catalog-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)
- In the chapter-03/order-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)

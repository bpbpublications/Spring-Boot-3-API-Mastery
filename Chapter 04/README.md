# Chapter 04

In this chapter we build a graphql service application with Spring for GraphQL. This microservice contains:
- GraphQL APIs for the orders (using REST API of order service and catalog service)
- GraphQL APIs for book (in memory db)
CRUD endpoints for the "order" domain object.

## Initialize GraphQL Service with Spring Initializr

Download the project structure from [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=21&groupId=com.easyshop&artifactId=graphql-service&name=graphql-service&description=GraphQL%20server%20for%20easyshop%20services&packageName=com.easyshop.graphqlservice&dependencies=graphql,web,validation,lombok,testcontainers) GUI.

Or you can use the REST API of Spring Initializr:

```bash
curl https://start.spring.io/starter.zip -d groupId=com.easyshop -d artifactId=graphql-service -d name=graphql-service -d description="GraphQL server for easyshop services" -d packageName=com.easyshop.graphqlservice -d dependencies=graphql,web,validation,lombok,testcontainers -d javaVersion=21 -d bootVersion=3.2.4 -d type=maven-project -o graphql-service.zip
```


## Run the project
- Start Docker
- In the chapter-04/infra/docker path, run `docker compose up -d`
- In the chapter-04/catalog-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)
- In the chapter-04/order-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)
- In the chapter-04/graphql-service path, run `./mvnw spring-boot:run` (Linux/Mac) or `mvnw.cmd spring boot:run` (Windows)

Enter http://localhost:8082/graphiql in a browser to use GraphiQL client and run some queries like this:
```graphql
query orderQuery{
  findOrderByCode(orderCode: "wrong-code") {
    status
    totalPrice
  }
}
```


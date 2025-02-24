# Chapter 07

In this chapter we will create a new microservice, edge-service who acts as an API Gateway (using Spring Cloud Gateway). \
Then, we will implement security using OIDC and OAuth2 protocols and Keycloak as identity Server. \
The edge service will be the OAuth2 Client and the catalog service will be the Resource Server.

## Initialize Shipping Service with Spring Initializr

Download the project structure from [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=21&groupId=com.easyshop&artifactId=edge-service&name=edge-service&description=Demo%20project%20for%20Spring%20Boot&packageName=com.easyshop.edgeservice&dependencies=cloud-gateway-reactive,cloud-resilience4j,oauth2-client,session,data-redis-reactive,lombok) GUI.

Or you can use the REST API of Spring Initializr:

```bash
curl https://start.spring.io/starter.zip -d groupId=com.easyshop -d artifactId=edge-service -d name=edge-service -d packageName=com.easyshop.edgeservice -d dependencies=cloud-gateway-reactive,cloud-resilience4j,oauth2-client,session,data-redis-reactive,lombok -d javaVersion=21 -d bootVersion=3.2.4 -d type=maven-project -o edge-service.zip
```

## Run the projects

From the infra/docker path, run:
```bash
docker compose up -d
```

Then, run the follow microservices:
- catalog service
- edge service

From your browser, open an incognito tab and type in the following URL: http://localhost:8090/products/LAP-ABCD. \
You will be redirected to the Keycloak login. After authenticating, you will see the API response correctly.

# Chapter 06

In this chapter we will implement asynchronous communication between the order service microservices and the 
new shipping service microservice, using Spring Cloud Stream and Apache Kafka.

## Initialize Shipping Service with Spring Initializr

Download the project structure from [Spring Initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=21&groupId=com.easyshop&artifactId=shipping-service&name=shipping-service&description=Demo%20project%20for%20Spring%20Boot&packageName=com.easyshop.shippingservice&dependencies=web,validation,data-jdbc,postgresql,kafka,cloud-stream,lombok,testcontainers) GUI.

Or you can use the REST API of Spring Initializr:

```bash
curl https://start.spring.io/starter.zip -d groupId=com.easyshop -d artifactId=shipping-service -d name=shipping-service -d packageName=com.easyshop.shippingservice -d dependencies=web,validation,data-jdbc,postgresql,kafka,cloud-stream,lombok,testcontainers -d javaVersion=21 -d bootVersion=3.2.4 -d type=maven-project -o shipping-service.zip
```

## Run the projects

From the infra/docker path, run:
```bash
docker compose up -d
```

Then, run the follow microservices:
- catalog service
- order service
- shipping service


Create an order with these products with POST API to test the correct communication between order service and shipping service:
```bash
http :8081/orders \
  Content-Type:application/json \
  "products[0][productCode]"=0001 \
  "products[0][quantity]":=2 \
  "products[1][productCode]"=0002 \
  "products[1][quantity]":=1
```

## Useful command for Kafka

### List topics
```shell
docker exec -t broker /usr/bin/kafka-topics --bootstrap-server localhost:9092 --list
```

### Describe a topic
```shell
docker exec -t broker /usr/bin/kafka-topics --bootstrap-server localhost:9092 --describe --topic shipment_event_topic
```

### See all messages from a topic
```shell
docker exec -t broker /usr/bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic shipment_event_topic --from-beginning --property print.key=true --property print.partition=true
```

### Send a message to a topic
```shell
docker exec -it broker /usr/bin/kafka-console-producer --bootstrap-server localhost:9092 --topic shipment_event_topic --property parse.key=true --property key.separator=:
```
not-exist:{"orderCode": "not-exist", "status": "DELIVERING"}
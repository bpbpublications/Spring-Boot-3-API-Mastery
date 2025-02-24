package com.easyshop.graphqlservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)
public class TestGraphqlServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(GraphqlServiceApplication::main).with(TestGraphqlServiceApplication.class).run(args);
	}

}

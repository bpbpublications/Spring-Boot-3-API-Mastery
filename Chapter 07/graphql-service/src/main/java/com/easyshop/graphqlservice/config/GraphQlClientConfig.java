package com.easyshop.graphqlservice.config;

import com.easyshop.graphqlservice.book.dto.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.HttpGraphQlClient;

@Configuration
@Slf4j
public class GraphQlClientConfig {

    @Bean
    HttpGraphQlClient httpGraphQlClient() {
        return HttpGraphQlClient.builder()
                .url("http://localhost:8082/graphql")
                .build();
    }

    @Bean
    ApplicationRunner applicationRunner(HttpGraphQlClient http) {
        return args -> {
            var query = """
					query bookDetails{
					  bookById(id: "book-1") {
					    id
					    name
					  }
					}
					""";

            http.document(query)
                    .retrieve("bookById")
                    .toEntity(Book.class)
                    .subscribe(book -> log.info("Book: {}", book));
        };
    }
}

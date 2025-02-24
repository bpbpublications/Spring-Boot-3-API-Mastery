package com.easyshop.graphqlservice.book.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Book(String id, String name, int pageCount, String authorId) {

    private static int currentIndex = 4;

    private static final List<Book> books = new ArrayList<>(Arrays.asList(
            new Book("book-1", "Spring Boot 3 API Mastery", 350, "author-1"),
            new Book("book-2", "Java 8 to 21", 320, "author-2"),
            new Book("book-3", "Java Persistence with NoSQL", 366, "author-3")
    ));

    public static Book getById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    public static List<Book> getAllByName(String name) {
        return books.stream()
                .filter(book -> book.name().equals(name))
                .toList();
    }

    public static Book saveBook(BookInput bookRequest) {
        var book = new Book("book-" + currentIndex++, bookRequest.name(), bookRequest.pageCount(), bookRequest.authorId());
        books.add(book);
        return book;
    }
}

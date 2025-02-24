package com.easyshop.graphqlservice.book.dto;

import java.util.Arrays;
import java.util.List;

public record Author(String id, String firstName, String lastName) {

    private static List<Author> authors = Arrays.asList(
            new Author("author-1", "Vincenzo", "Racca"),
            new Author("author-2", "Shai", "Almog"),
            new Author("author-3", "Otávio", "Santana")
    );

    public static Author getById(String id) {
        return authors.stream()
                .filter(author -> author.id().equals(id))
                .findFirst()
                .orElse(null);
    }
}

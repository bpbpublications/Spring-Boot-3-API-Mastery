package com.easyshop.graphqlservice.book.dto;

public record BookInput(String name, int pageCount, String authorId) {
}

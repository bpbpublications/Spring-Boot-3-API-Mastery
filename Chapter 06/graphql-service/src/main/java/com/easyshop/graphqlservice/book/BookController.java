package com.easyshop.graphqlservice.book;

import com.easyshop.graphqlservice.book.dto.Author;
import com.easyshop.graphqlservice.book.dto.Book;
import com.easyshop.graphqlservice.book.dto.BookInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class BookController {

    @QueryMapping
    public Book bookById(@Argument String id) {
        return Book.getById(id);
    }

    @QueryMapping
    public List<Book> booksByName(@Argument String name) {
        return Book.getAllByName(name);
    }

    @SchemaMapping
    public Author author(Book book) {
        return Author.getById(book.authorId());
    }

    @MutationMapping
    public Book saveBook(@Argument BookInput book) {
        return Book.saveBook(book);
    }
}

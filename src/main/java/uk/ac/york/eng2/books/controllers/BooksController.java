package uk.ac.york.eng2.books.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import uk.ac.york.eng2.books.domain.Book;
import uk.ac.york.eng2.books.dto.BookDTO;
import uk.ac.york.eng2.books.repositories.BooksRepository;

import java.net.URI;

@Controller("/books")
public class BooksController {
    @Inject
    public BooksRepository repository;

    @Get("/")
    public Iterable<Book> list()
    {
        return repository.findAll();
    }

    @Post("/")
    public HttpResponse<Void> add(@Body BookDTO bookDetails) {
        Book book = new Book(bookDetails.getTitle(), bookDetails.getYear());
        repository.save(book);
        return HttpResponse.created(URI.create("/books/" + book.getId()));
    }
}

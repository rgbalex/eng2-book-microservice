package uk.ac.york.eng2.books.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MutableHttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Inject;
import uk.ac.york.eng2.books.domain.Book;
import uk.ac.york.eng2.books.dto.BookDTO;
import uk.ac.york.eng2.books.repositories.BooksRepository;

import java.net.URI;

import javax.transaction.Transactional;

@Controller("/books")
public class BooksController {
    @Inject
    public BooksRepository repository;

    @Get("/")
    public Iterable<Book> list()
        { return repository.findAll(); }

    @Post("/")
    public HttpResponse<Void> add(@Body BookDTO bookDetails) {
        Book book = new Book(bookDetails.getTitle(), bookDetails.getYear());
        repository.save(book);
        return HttpResponse.created(URI.create("/books/" + book.getId()));
    }

    @Get("/{id}")
    public Book getBook(long id) {
        return repository.findById(id).orElse(null);
    }

    @Put("/{id}")
    @Transactional
    public HttpResponse<Void> updateBook(long id, @Body BookDTO book)
    {
        Book foundBook = repository.findById(id).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }
        
        foundBook.setTitle(book.getTitle());
        foundBook.setYear(book.getYear());
        
        repository.update(foundBook);
        
        return HttpResponse.ok();
    }

    @Delete("/{id}")
    @Transactional
    public HttpResponse<Void> deleteBook(long id)
    {
        Book foundBook = repository.findById(id).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }
        
        repository.delete(foundBook);
        
        return HttpResponse.ok();
    }
}

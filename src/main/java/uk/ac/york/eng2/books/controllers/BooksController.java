package uk.ac.york.eng2.books.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;

import jakarta.inject.Inject;

import uk.ac.york.eng2.books.domain.Book;
import uk.ac.york.eng2.books.domain.User;
import uk.ac.york.eng2.books.dto.BookDTO;
import uk.ac.york.eng2.books.events.BooksProducer;
import uk.ac.york.eng2.books.repositories.BooksRepository;
import uk.ac.york.eng2.books.repositories.UsersRepository;

import java.net.URI;

import javax.transaction.Transactional;

@Controller("/books")
public class BooksController {
    @Inject
    public BooksRepository bookRepository;

    @Inject
    public UsersRepository userRepository;

    @Inject
    public BooksProducer booksProducer;

    @Get("/")
    public Iterable<Book> list()
        { return bookRepository.findAll(); }

    @Get("/{id}/readers")
    public Iterable<User> listReaders(long id)
    { 
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) 
            { return null; }

        return book.getReaders();
    }

    @Post("/")
    public HttpResponse<Void> add(@Body BookDTO bookDetails) {
        Book book = new Book(bookDetails.getTitle(), bookDetails.getYear());
        bookRepository.save(book);
        return HttpResponse.created(URI.create("/books/" + book.getId()));
    }

    @Get("/{id}")
    public Book getBook(long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Transactional
    @Put("/{id}")
    public HttpResponse<Void> updateBook(long id, @Body BookDTO book)
    {
        Book foundBook = bookRepository.findById(id).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }
        
        foundBook.setTitle(book.getTitle());
        foundBook.setYear(book.getYear());
        
        bookRepository.update(foundBook);
        
        return HttpResponse.ok();
    }

    @Transactional
    @Put("/{bookId}/readers/{readerId}")
    public HttpResponse<Void> updateBookReaders(long bookId, long readerId)
    {
        Book foundBook = bookRepository.findById(bookId).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }

        User foundUser = userRepository.findById(readerId).orElse(null);
        if (foundUser == null) {
            return HttpResponse.notFound();
        }

        if (foundBook.addReader(foundUser))
        {   
            bookRepository.update(foundBook);
            booksProducer.readBook(readerId, foundBook);
        }

        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{id}")
    public HttpResponse<Void> deleteBook(long id)
    {
        Book foundBook = bookRepository.findById(id).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }
        
        bookRepository.delete(foundBook);
        
        return HttpResponse.ok();
    }

    @Transactional
    @Delete("/{bookId}/readers/{readerId}")
    public HttpResponse<Void> deleteBookReader(long bookId, long readerId)
    {
        Book foundBook = bookRepository.findById(bookId).orElse(null);
        if (foundBook == null) {
            return HttpResponse.notFound();
        }

        User foundUser = userRepository.findById(readerId).orElse(null);
        if (foundUser == null) {
            return HttpResponse.notFound();
        }

        foundBook.removeReader(foundUser);
        bookRepository.update(foundBook);

        return HttpResponse.ok();
    }
}

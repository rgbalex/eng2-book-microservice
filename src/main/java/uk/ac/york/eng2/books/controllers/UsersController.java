package uk.ac.york.eng2.books.controllers;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import uk.ac.york.eng2.books.domain.User;
import uk.ac.york.eng2.books.dto.UserDTO;
import uk.ac.york.eng2.books.repositories.UsersRepository;

import java.net.URI;

import javax.transaction.Transactional;

@Controller("/users")
public class UsersController {
    @Inject
    public UsersRepository repository;

    @Get("/")
    public Iterable<User> list()
        { return repository.findAll(); }

    @Post("/")
    public HttpResponse<Void> add(@Body UserDTO userDetails) {
        User user = new User(userDetails.getName(), userDetails.getAge());
        repository.save(user);
        return HttpResponse.created(URI.create("/users/" + user.getId()));
    }

    @Get("/{id}")
    public User getUser(long id) {
        return repository.findById(id).orElse(null);
    }

    @Put("/{id}")
    @Transactional
    public HttpResponse<Void> updateUser(long id, @Body UserDTO user)
    {
        User foundUser = repository.findById(id).orElse(null);
        if (foundUser == null) {
            return HttpResponse.notFound();
        }
        
        foundUser.setName(user.getName());
        foundUser.setAge(user.getAge());
        
        repository.update(foundUser);
        
        return HttpResponse.ok();
    }

    @Delete("/{id}")
    @Transactional
    public HttpResponse<Void> deleteUser(long id)
    {
        User foundUser = repository.findById(id).orElse(null);
        if (foundUser == null) {
            return HttpResponse.notFound();
        }
        
        repository.delete(foundUser);
        
        return HttpResponse.ok();
    }
}

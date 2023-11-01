package uk.ac.york.eng2.books.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.micronaut.serde.annotation.Serdeable;

@Entity
@Serdeable
public class Book {

    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private Integer year;
    @JsonIgnore
    @ManyToMany
    private Set<User> readers;


    public Book(String title, Integer year) {
        this.title = title;
        this.year = year;
    }

    public Book() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    // Required for ManyToMany to work
    public Set<User> getReaders() {
        return readers;
    }

    // Turns out this is also required for Micronaut not to throw a fit.
    public void setReaders(Set<User> readers) {
        this.readers = readers;
    }

    public void addReader(User user) {
        readers.add(user);
    }
    
}

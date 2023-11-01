package uk.ac.york.eng2.books.repositories;
import uk.ac.york.eng2.books.domain.Book;

import java.util.Optional;

import javax.validation.constraints.NotNull;

import io.micronaut.data.annotation.Join;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {
    @Join(value = "readers", type = Join.Type.LEFT_FETCH)
    @Override
    Optional<Book> findById(@NotNull Long id);
}

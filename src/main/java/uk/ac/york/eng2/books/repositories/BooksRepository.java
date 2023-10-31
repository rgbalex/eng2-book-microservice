package uk.ac.york.eng2.books.repositories;
import uk.ac.york.eng2.books.domain.Book;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface BooksRepository extends CrudRepository<Book, Long> {

}

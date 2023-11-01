package uk.ac.york.eng2.books.repositories;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import uk.ac.york.eng2.books.domain.User;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {

}

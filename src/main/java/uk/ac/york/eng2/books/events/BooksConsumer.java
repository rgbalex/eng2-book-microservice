package uk.ac.york.eng2.books.events;

import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import uk.ac.york.eng2.books.domain.Book;

@KafkaListener
public class BooksConsumer {

    @Topic(BooksProducer.BOOK_READ_TOPIC)
    public void readBook(@KafkaKey long id, Book b) {
        System.out.println("Book read: " + id);
    }
}

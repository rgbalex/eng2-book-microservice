package uk.ac.york.eng2.books.events;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.Topic;

import uk.ac.york.eng2.books.domain.Book;

@KafkaClient
public interface BooksProducer {
    
    String BOOK_READ_TOPIC = "book-read";

    @Topic(BOOK_READ_TOPIC)
    public void readBook(@KafkaKey long Id, Book b);
}

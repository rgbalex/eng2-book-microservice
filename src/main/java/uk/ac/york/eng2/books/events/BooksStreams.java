package uk.ac.york.eng2.books.events;

import java.time.Duration;
import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindowedKStream;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;

import io.micronaut.configuration.kafka.serde.SerdeRegistry;
import io.micronaut.configuration.kafka.streams.ConfiguredStreamBuilder;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import uk.ac.york.eng2.books.domain.Book;

@Factory
public class BooksStreams {
    
    public String TOPIC_READ_BY_DAY = "book-read-by-day";

    @Inject
    private SerdeRegistry serdeRegistry;

    @Singleton 
    public KStream<WindowedIdentifier, Long> readByDay(ConfiguredStreamBuilder builder){
        Properties props = builder.getConfiguration();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "books-streams");
        props.put(StreamsConfig.PROCESSING_GUARANTEE_CONFIG, StreamsConfig.EXACTLY_ONCE);


        // (1l, Book {...})
        KStream<Long, Book> stream = 
            builder.stream(BooksProducer.BOOK_READ_TOPIC, Consumed.with(Serdes.Long(), serdeRegistry.getSerde(Book.class)));
        
        // (1l, [Book {...}, Book {...}, ...])
        KGroupedStream<Long, Book> groupedStream = stream.groupByKey();

        // (1l, [Book {...}, Book {...}, ...]) subgrouped by day
        TimeWindowedKStream<Long, Book> groupedByDayStream = 
            groupedStream.windowedBy(TimeWindows.of(Duration.ofDays(1)).advanceBy(Duration.ofDays(1)));

        // Table from book ID + window to latest count
        KTable<Windowed<Long>, Long> countByKey = groupedByDayStream.count();
   
        // Stream of updates from the table
        KStream<Windowed<Long>, Long> countStream = countByKey.toStream();

        // Stream of updates from the table where keys are WindowIdentifier objects in JSON format
        KStream<WindowedIdentifier, Long> jsonCountStream = countStream.selectKey(new KeyValueMapper<Windowed<Long>, Long, WindowedIdentifier>() {
            @Override
            public WindowedIdentifier apply(Windowed<Long> key, Long value) {
                return new WindowedIdentifier(key.key(), key.window().start(), key.window().end());
            }
        });

        jsonCountStream.to(TOPIC_READ_BY_DAY, Produced.with(serdeRegistry.getSerde(WindowedIdentifier.class), Serdes.Long()));

        return jsonCountStream;
    }
}

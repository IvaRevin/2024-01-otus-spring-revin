package ru.otus.hw.processors;

import jakarta.annotation.Nonnull;
import org.springframework.batch.item.ItemProcessor;
import ru.otus.hw.models.jpa.JpaBook;
import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;
import java.util.stream.Collectors;

public class BookProcessor implements ItemProcessor<JpaBook, MongoBook> {
    @Override
    public MongoBook process(@Nonnull JpaBook item) {
        AuthorProcessor authorProcessor = new AuthorProcessor();
        MongoAuthor author = authorProcessor.process(item.getAuthor());

        GenreProcessor genreProcessor = new GenreProcessor();
        List<MongoGenre> genreList = item.getGenres().stream()
            .map(genreProcessor::process)
            .collect(Collectors.toList());

        return new MongoBook(String.valueOf(item.getId()), item.getTitle(), author, genreList);
    }
}

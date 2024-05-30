package ru.otus.hw.repositories;

import jakarta.annotation.Nonnull;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.Book;

import java.util.Optional;

public interface BookRepository extends MongoRepository<Book, String> {

    @Nonnull
    Optional<Book> findById(@Nonnull String id);
}

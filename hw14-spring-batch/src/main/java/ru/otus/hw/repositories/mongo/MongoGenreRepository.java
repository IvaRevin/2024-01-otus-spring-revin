package ru.otus.hw.repositories.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.mongo.MongoGenre;

import java.util.List;
import java.util.Set;

public interface MongoGenreRepository extends MongoRepository<MongoGenre, String> {

    List<MongoGenre> findAllByIdIn(Set<String> ids);
}

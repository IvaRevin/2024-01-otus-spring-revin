package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с жанрами")
@DataMongoTest
public class MongoGenreRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    @DisplayName("Очищает тестовую бд перед тестом.")
    public void dropTable() {

        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Проверяет, что метод findAllByIdIn возвращает корректный список объектов Genre.")
    void findAllByIdsShouldReturnGenres() {
        Genre genre1 = new Genre(null, "Genre_1");
        Genre genre2 = new Genre(null, "Genre_2");
        Genre genre3 = new Genre(null, "Genre_3");
        mongoTemplate.save(genre1);
        mongoTemplate.save(genre2);
        mongoTemplate.save(genre3);

        Set<String> ids = Set.of(genre1.getId(), genre2.getId(), genre3.getId());
        List<Genre> genres = genreRepository.findAllById(ids);

        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(Genre::getName)
            .containsExactlyInAnyOrder("Genre_1", "Genre_2", "Genre_3");
    }
}

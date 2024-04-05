package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с жанрами")
@DataJpaTest
@Import({JpaGenreRepository.class})
public class JpaGenreRepositoryTest {

    @Autowired
    private JpaGenreRepository genreRepository;

    @Test
    @DisplayName("Проверяет, что метод findAll возвращает корректный список объектов Genre.")
    void findAllByIdsShouldReturnGenres() {
        Set<Long> ids = Set.of(1L, 2L, 3L);
        List<Genre> genres = genreRepository.findAllByIds(ids);

        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(Genre::getName)
            .containsExactlyInAnyOrder("Genre_1", "Genre_2", "Genre_3");
    }

}

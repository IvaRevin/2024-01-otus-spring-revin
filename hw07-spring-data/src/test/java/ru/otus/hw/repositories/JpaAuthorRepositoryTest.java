package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с авторами ")
@DataJpaTest
public class JpaAuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Test
    @DisplayName("Проверяет, что метод findById возвращает корректный объект Author.")
    void findByIdWhenExistsShouldReturnAuthor() {
        Optional<Author> foundAuthor = authorRepository.findById(1L);

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getFullName()).isEqualTo("Author_1");
    }

    @Test
    @DisplayName("Проверяет, что метод findAll возвращает корректный список объектов Author.")
    void findAllShouldReturnAllAuthors() {
        var authors = authorRepository.findAll();

        assertThat(authors).hasSize(3);
        assertThat(authors).extracting(Author::getFullName)
            .containsExactlyInAnyOrder("Author_1", "Author_2", "Author_3");
    }
}

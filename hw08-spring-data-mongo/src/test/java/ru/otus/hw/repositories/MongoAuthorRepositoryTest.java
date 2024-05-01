package ru.otus.hw.repositories;

import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с авторами")
@DataMongoTest
public class MongoAuthorRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorRepository authorRepository;

    @BeforeEach
    @DisplayName("Очищает тестовую бд перед тестом.")
    public void dropTable() {

        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Проверяет, что метод findById возвращает корректный объект Author.")
    void findByIdWhenExistsShouldReturnAuthor() {

        Author author = new Author("1", "Author_1");
        mongoTemplate.save(author);

        Optional<Author> foundAuthor = authorRepository.findById("1");

        assertThat(foundAuthor).isPresent();
        assertThat(foundAuthor.get().getFullName()).isEqualTo("Author_1");
    }

    @Test
    @DisplayName("Проверяет, что метод findAll возвращает корректный список объектов Author.")
    void findAllShouldReturnAllAuthors() {

        List<Author> authors = List.of(
            new Author("1", "Author_1"),
            new Author("2", "Author_2"),
            new Author("3", "Author_3")
        );
        authors.forEach(mongoTemplate::save);

        var foundAuthors = authorRepository.findAll();

        assertThat(foundAuthors).hasSize(3);
        assertThat(foundAuthors).extracting(Author::getFullName)
            .containsExactlyInAnyOrder("Author_1", "Author_2", "Author_3");
    }
}

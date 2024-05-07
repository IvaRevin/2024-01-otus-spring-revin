package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с книгами")
@DataMongoTest
class MongoBookRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    @DisplayName("Очищает тестовую бд перед тестом.")
    public void dropTable() {

        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Проверяет, что метод findById возвращает корректный объект Book с установленными связями.")
    void shouldReturnBook() {

        Author author = new Author("1", "Author_1");
        Genre genre1 = new Genre("1", "Genre_1");
        Genre genre2 = new Genre("2", "Genre_2");
        mongoTemplate.save(author);
        mongoTemplate.save(genre1);
        mongoTemplate.save(genre2);
        Book book = new Book("1", "BookTitle_1", author, List.of(genre1, genre2));
        mongoTemplate.save(book);

        Optional<Book> foundBook = bookRepository.findById("1");

        assertThat(foundBook).isPresent();
        assertThat(foundBook.get().getTitle()).isEqualTo("BookTitle_1");
        assertThat(foundBook.get().getAuthor().getFullName()).isEqualTo("Author_1");
        Set<String> genreNames = foundBook.get().getGenres().stream()
            .map(Genre::getName)
            .collect(Collectors.toSet());
        assertThat(genreNames).containsExactlyInAnyOrder("Genre_1", "Genre_2");
    }

    @Test
    @DisplayName("Подтверждает, что метод findAll возвращает список всех книг.")
    void shouldReturnAllBooks() {

        Author author = new Author("1", "Author_1");
        mongoTemplate.save(author);
        Book book1 = new Book("1", "BookTitle_1", author, List.of());
        Book book2 = new Book("2", "BookTitle_2", author, List.of());
        Book book3 = new Book("3", "BookTitle_3", author, List.of());
        mongoTemplate.save(book1);
        mongoTemplate.save(book2);
        mongoTemplate.save(book3);

        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(3);
        assertThat(books).extracting(Book::getTitle)
            .containsExactlyInAnyOrder("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @Test
    @DisplayName("Сохранение новой книги с установленным автором и жанрами.")
    void saveBookShouldPersist() {
        Author author = new Author("1", "Existing Author");
        Genre genre = new Genre("1", "Existing Genre");
        mongoTemplate.save(author);
        mongoTemplate.save(genre);

        Book newBook = new Book(null, "New Book", author, List.of(genre));
        Book savedBook = bookRepository.save(newBook);

        Optional<Book> fetchedBook = bookRepository.findById(savedBook.getId());
        assertThat(fetchedBook).isPresent();
        assertThat(fetchedBook.get().getTitle()).isEqualTo("New Book");
    }

    @Test
    @DisplayName("Проверяет удаление книги по ID.")
    void deleteByIdWhenBookExistsShouldRemoveBook() {
        String bookIdToDelete = "1";
        Book book = new Book(bookIdToDelete, "Book to Delete", null, List.of());
        mongoTemplate.save(book);

        bookRepository.deleteById(bookIdToDelete);

        Optional<Book> deletedBook = bookRepository.findById(bookIdToDelete);
        assertThat(deletedBook).isNotPresent();
    }
}

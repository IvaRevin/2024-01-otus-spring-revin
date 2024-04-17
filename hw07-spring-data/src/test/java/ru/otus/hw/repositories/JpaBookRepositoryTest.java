package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с книгами ")
@DataJpaTest
@Import({JpaBookRepository.class})
class JpaBookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaBookRepository bookRepository;

    @Test
    @DisplayName("Проверяет, что метод findById возвращает корректный объект Book с установленными связями.")
    void shouldReturnBook() {
        Optional<Book> foundBook = bookRepository.findById(1L);

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
        List<Book> books = bookRepository.findAll();

        assertThat(books).hasSize(3);
        assertThat(books).extracting(Book::getTitle)
            .containsExactlyInAnyOrder("BookTitle_1", "BookTitle_2", "BookTitle_3");
    }

    @Test
    @DisplayName("Сохранение новой книги с установленным автором и жанрами.")
    void saveBookShouldPersist() {
        Book newBook = new Book();
        newBook.setTitle("New Book");
        newBook.setAuthor(entityManager.find(Author.class, 1L));
        newBook.setGenres(List.of(entityManager.find(Genre.class, 1L)));

        Book savedBook = bookRepository.save(newBook);

        assertThat(entityManager.find(Book.class, savedBook.getId())).isNotNull();
        assertThat(savedBook.getTitle()).isEqualTo("New Book");
    }

    @Test
    @DisplayName("Проверяет удаление книги по ID.")
    void deleteByIdWhenBookExistsShouldRemoveBook() {
        long bookIdToDelete = 1L;
        assertThat(entityManager.find(Book.class, bookIdToDelete)).isNotNull();

        bookRepository.deleteById(bookIdToDelete);

        assertThat(entityManager.find(Book.class, bookIdToDelete)).isNull();
    }
}

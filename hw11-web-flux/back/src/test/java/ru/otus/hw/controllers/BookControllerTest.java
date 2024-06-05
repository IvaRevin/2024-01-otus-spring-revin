package ru.otus.hw.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.controlles.BookController;
import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(BookController.class)
public class BookControllerTest {

    @MockBean
    private BookController bookController;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private AuthorRepository authorRepository;

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(bookController).build();
    }

    @Test
    @DisplayName("Получение списка всех книг")
    void getBookList() {
        Book book = new Book("1", "Test Book", new Author("1", "Test Author"), List.of(new Genre("1", "Test Genre")));
        when(bookRepository.findAll()).thenReturn(Flux.just(book));

        webTestClient.get()
            .uri("/api/books")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(BookDTO.class)
            .value(bookList -> assertThat(bookList).hasSize(1));
    }

    @Test
    @DisplayName("Получение книги по ID")
    void getBookById() {
        Book book = new Book("1", "Test Book", new Author("1", "Test Author"), List.of(new Genre("1", "Test Genre")));
        when(bookRepository.findById("1")).thenReturn(Mono.just(book));

        webTestClient.get()
            .uri("/api/books/1")
            .exchange()
            .expectStatus().isOk()
            .expectBody(BookDTO.class)
            .value(response -> {
                assertThat(response.getTitle()).isEqualTo("Test Book");
            });
    }

    @Test
    @DisplayName("Редактирование книги")
    void editBook() {
        BookEditDTO bookEditDTO = new BookEditDTO("1", "Updated Book", "1", new String[]{"1"});
        Book book = new Book("1", "Test Book", new Author("1", "Test Author"), List.of(new Genre("1", "Test Genre")));
        Author author = new Author("1", "Test Author");
        Genre genre = new Genre("1", "Test Genre");

        when(bookRepository.findById("1")).thenReturn(Mono.just(book));
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(genreRepository.findAllByIdIn(Set.of("1"))).thenReturn(Flux.just(genre));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.patch()
            .uri("/api/books/1")
            .bodyValue(bookEditDTO)
            .exchange()
            .expectStatus().isAccepted()
            .expectBody(BookDTO.class)
            .value(response -> assertThat(response.getTitle()).isEqualTo("Test Book"));
    }

    @Test
    @DisplayName("Создание книги")
    void createBook() {
        BookCreateDTO bookCreateDTO = new BookCreateDTO("1", "New Book", "1", new String[]{"1"});
        Book book = new Book("1", "New Book", new Author("1", "Test Author"), List.of(new Genre("1", "Test Genre")));
        Author author = new Author("1", "Test Author");
        Genre genre = new Genre("1", "Test Genre");

        when(bookRepository.findById("1")).thenReturn(Mono.empty());
        when(authorRepository.findById("1")).thenReturn(Mono.just(author));
        when(genreRepository.findAllByIdIn(Set.of("1"))).thenReturn(Flux.just(genre));
        when(bookRepository.save(ArgumentMatchers.any(Book.class))).thenReturn(Mono.just(book));

        webTestClient.post()
            .uri("/api/books")
            .bodyValue(bookCreateDTO)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(BookDTO.class)
            .value(response -> assertThat(response.getTitle()).isEqualTo("New Book"));
    }

    @Test
    @DisplayName("Удаление книги по ID")
    void deleteBook() {
        when(bookRepository.deleteById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
            .uri("/api/books/1")
            .exchange()
            .expectStatus().isNoContent();
    }
}

package ru.otus.hw.controlles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;
import ru.otus.hw.models.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/books")
    public Flux<BookDTO> getBookList() {
        return this.bookRepository.findAll()
            .map(BookDTO::bookToDto);
    }

    @GetMapping("/api/books/{id}")
    public Mono<ResponseEntity<BookDTO>> getBook(@PathVariable(value = "id", required = false) String id) {
        return this.bookRepository.findById(id)
            .map(BookDTO::bookToDto)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<BookDTO>> editBook(@PathVariable("id") String id,
                              @Valid @RequestBody BookEditDTO book) {
        book.setId(id);

        String bookId = book.getId();
        String authorId = book.getAuthorId();
        Set<String> genresIds = new HashSet<>(List.of(book.getGenreIds()));

        return this.bookRepository.findById(bookId)
            .flatMap(findedBook ->
                this.authorRepository.findById(authorId).flatMap(author ->
                    this.genreRepository.findAllByIdIn(genresIds).collectList().flatMap(genres ->
                        this.bookRepository.save(
                            Book.builder()
                                .id(book.getId())
                                .title(book.getTitle())
                                .author(author)
                                .genres(genres)
                                .build()
                        )
                    )
                )
            ).map(BookDTO::bookToDto)
            .map(updatedBook -> new ResponseEntity<>(updatedBook, HttpStatus.ACCEPTED))
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<BookDTO>> createBook(@Valid @RequestBody BookCreateDTO book) {

        String id = book.getId();
        String authorId = book.getAuthorId();
        Set<String> genresIds = new HashSet<>(List.of(book.getGenreIds()));

        return this.bookRepository.findById(id)
            .flatMap(findedBook ->
                this.authorRepository.findById(authorId).flatMap(author ->
                    this.genreRepository.findAllByIdIn(genresIds).collectList().flatMap(genres ->
                        this.bookRepository.save(
                            Book.builder()
                                .title(book.getTitle())
                                .author(author)
                                .genres(genres)
                                .build()
                        )
                    )
                )
            ).map(BookDTO::bookToDto)
            .map(createdBook -> new ResponseEntity<>(createdBook, HttpStatus.ACCEPTED))
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteBook(@PathVariable("id") String id) {
        return this.bookRepository.deleteById(id)
            .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }
}

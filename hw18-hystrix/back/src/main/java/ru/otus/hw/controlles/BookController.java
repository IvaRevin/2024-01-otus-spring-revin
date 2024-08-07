package ru.otus.hw.controlles;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;
import ru.otus.hw.services.BookServiceImpl;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    @CircuitBreaker(name = "bookBreaker", fallbackMethod = "unknownBookListFallback")
    @GetMapping("/api/books")
    public List<BookDTO> getBookList() {
        return bookService.findAll();
    }

    @CircuitBreaker(name = "bookBreaker", fallbackMethod = "unknownBookFallback")
    @GetMapping("/api/books/{id}")
    public BookDTO getBook(@PathVariable(value = "id", required = false) Long id) {
        return bookService.findById(id).orElse(null);
    }

    @PatchMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public BookDTO editBook(@PathVariable("id") Long id,
                              @Valid @RequestBody BookEditDTO book) {
        book.setId(id);
        return bookService.update(book);
    }

    @PostMapping("/api/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDTO createBook(@Valid @RequestBody BookCreateDTO book) {
        return bookService.create(book);
    }

    @DeleteMapping("/api/books/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }

    public BookDTO unknownBookFallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new BookDTO(null, "NaN", null, null);
    }

    public List<BookDTO> unknownBookListFallback(Exception ex) {
        log.error("List book error:" + ex.getMessage(), ex);
        return Collections.emptyList();
    }
}

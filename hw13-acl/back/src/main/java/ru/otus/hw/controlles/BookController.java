package ru.otus.hw.controlles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;
import ru.otus.hw.services.BookServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private final BookServiceImpl bookService;

    @GetMapping("/books")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<BookDTO> getBookList() {
        return bookService.findAll();
    }

    @GetMapping("/books/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public BookDTO getBook(@PathVariable(value = "id", required = false) Long id) {
        return bookService.findById(id).orElse(null);
    }

    @PatchMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public BookDTO editBook(@PathVariable("id") Long id,
                              @Valid @RequestBody BookEditDTO book) {
        book.setId(id);
        return bookService.update(book);
    }

    @PostMapping("/books")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.CREATED)
    public BookDTO createBook(@Valid @RequestBody BookCreateDTO book) {
        return bookService.create(book);
    }

    @DeleteMapping("/books/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable("id") Long id) {
        bookService.deleteById(id);
    }
}

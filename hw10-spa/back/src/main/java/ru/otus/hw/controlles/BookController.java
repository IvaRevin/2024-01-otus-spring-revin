package ru.otus.hw.controlles;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;

import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookServiceImpl bookService;

    private final AuthorServiceImpl authorService;

    private final GenreServiceImpl genreService;

    @GetMapping("/")
    public String getBookList(Model model) {
        List<BookDTO> bookDTOList = bookService.findAll();
        model.addAttribute("books", bookDTOList);
        return "books/books";
    }

    @GetMapping("/edit_book")
    public String editBook(
        @RequestParam(value = "id", required = false) Long id,
        Model model
    ) {
        if (id == null) {
            BookCreateDTO book = new BookCreateDTO(null, null, null, new Long[0]);
            model.addAttribute("book", book);
        } else {
            bookService.findById(id)
                .ifPresent(book -> model.addAttribute("book", BookEditDTO.fromBookDto(book)));
        }

        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("genres", genreService.findAll());
        return "books/edit_book";
    }

    @PostMapping("/update_book")
    public String updateBook(
        @Valid @ModelAttribute("book") BookEditDTO book,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/edit_book?id=%d".formatted(book.getId());
        }

        bookService.update(book);
        return "redirect:/";
    }


    @PostMapping("/create_book")
    public String createBook(
        @Valid @ModelAttribute("book") BookCreateDTO book,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/edit_book?id=%d".formatted(book.getId());
        }

        bookService.create(book);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteBook(@RequestParam("id") long id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}

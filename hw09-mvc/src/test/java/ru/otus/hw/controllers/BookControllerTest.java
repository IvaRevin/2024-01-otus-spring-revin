package ru.otus.hw.controllers;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.otus.hw.controlles.BookController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.AuthorServiceImpl;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DisplayName("Тесты контроллера связанного с книгами.")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    @MockBean
    private AuthorServiceImpl authorService;

    @MockBean
    private GenreServiceImpl genreService;

    private BookDTO sampleBook;

    @BeforeEach
    void setUp() {
        AuthorDTO author = new AuthorDTO(1L, "some name");
        List<GenreDTO> genreDTOList = new ArrayList<>();
        genreDTOList.add(new GenreDTO(1L, "some name 1"));
        genreDTOList.add(new GenreDTO(2L, "some name 2"));
        sampleBook = new BookDTO(1L, "Sample Book", author, genreDTOList);
    }

    @Test
    @DisplayName("Должен отобразить все книги")
    void allBooksList_ShouldAddBooksToModel_AndReturnBooksView() throws Exception {
        List<BookDTO> books = Collections.singletonList(sampleBook);
        given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("books/books"));
    }

    @Test
    @DisplayName("Должно пройти успешное редактирование книги")
    void editBook_WhenIdIsProvided_ShouldAddBookToModel_AndReturnEditView() throws Exception {
        given(bookService.findById(1L)).willReturn(java.util.Optional.of(sampleBook));
        given(authorService.findAll()).willReturn(List.of());
        given(genreService.findAll()).willReturn(List.of());

        mockMvc.perform(get("/edit_book").param("id", "1"))
            .andExpect(status().isOk())
            .andExpect(view().name("books/edit_book"));
    }

    @Test
    @DisplayName("Должно пройти успешное создание книги")
    void createBook_WhenPostRequest_ExpectRedirection() throws Exception {
        mockMvc.perform(post("/create_book")
                .param("title", "New Book")
                .param("authorId", "1")
                .param("genreIds", "1", "2"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        verify(bookService).create(new BookCreateDTO(null, "New Book", 1L, new Long[]{1L, 2L}));
    }
}

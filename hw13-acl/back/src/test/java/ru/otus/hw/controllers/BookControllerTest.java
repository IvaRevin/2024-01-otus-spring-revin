package ru.otus.hw.controllers;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.controlles.BookController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.BookServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DisplayName("Тесты контроллера связанного с книгами.")
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookService;

    private BookDTO sampleBook;

    @BeforeEach
    void setUp() {
        AuthorDTO author = new AuthorDTO(1L, "Author_1");
        List<GenreDTO> genres = new ArrayList<>();
        genres.add(new GenreDTO(1L, "Genre_1"));
        genres.add(new GenreDTO(2L, "Genre_2"));
        sampleBook = new BookDTO(1L, "BookTitle_1", author, genres);
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен отобразить все книги")
    void allBooksList_ShouldReturnBooks() throws Exception {
        List<BookDTO> books = Arrays.asList(
            new BookDTO(1L, "BookTitle_1", new AuthorDTO(1L, "Author_1"), Arrays.asList(new GenreDTO(1L, "Genre_1"), new GenreDTO(2L, "Genre_2"))),
            new BookDTO(2L, "BookTitle_2", new AuthorDTO(2L, "Author_2"), Arrays.asList(new GenreDTO(3L, "Genre_3"), new GenreDTO(4L, "Genre_4"))),
            new BookDTO(3L, "BookTitle_3", new AuthorDTO(3L, "Author_3"), Arrays.asList(new GenreDTO(5L, "Genre_5"), new GenreDTO(6L, "Genre_6")))
        );
        given(bookService.findAll()).willReturn(books);

        mockMvc.perform(get("/api/books"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'title':'BookTitle_1','author':{'id':1,'fullName':'Author_1'},'genres':[{'id':1,'name':'Genre_1'},{'id':2,'name':'Genre_2'}]},{'id':2,'title':'BookTitle_2','author':{'id':2,'fullName':'Author_2'},'genres':[{'id':3,'name':'Genre_3'},{'id':4,'name':'Genre_4'}]},{'id':3,'title':'BookTitle_3','author':{'id':3,'fullName':'Author_3'},'genres':[{'id':5,'name':'Genre_5'},{'id':6,'name':'Genre_6'}]}]"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должно получить информацию о книге по ID")
    void getBookById_WhenIdIsProvided_ShouldReturnBook() throws Exception {
        given(bookService.findById(1L)).willReturn(java.util.Optional.of(sampleBook));

        mockMvc.perform(get("/api/books/1"))
            .andExpect(status().isOk())
            .andExpect(content().json("{'id':1,'title':'BookTitle_1','author':{'id':1,'fullName':'Author_1'},'genres':[{'id':1,'name':'Genre_1'},{'id':2,'name':'Genre_2'}]}"));
    }

    @Test
    @DisplayName("Должен возвращать 401 при удалении книги без роли")
    void deleteBookWith401() throws Exception {
        mockMvc.perform(delete("/api/books/1").with(csrf()))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен удалить книгу с ролью ADMIN")
    void deleteBookWithAdminRole() throws Exception {
        mockMvc.perform(delete("/api/books/1").with(csrf()))
            .andExpect(status().isNoContent());
    }
}

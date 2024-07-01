package ru.otus.hw.controllers;

import static org.mockito.BDDMockito.given;
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
import ru.otus.hw.controlles.GenreController;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(GenreController.class)
@DisplayName("Тесты контроллера связанного с жанрами.")
public class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenreServiceImpl genreService;

    private List<GenreDTO> sampleGenres;

    @BeforeEach
    void setUp() {
        sampleGenres = Arrays.asList(
            new GenreDTO(1L, "Genre 1"),
            new GenreDTO(2L, "Genre 2")
        );
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен отобразить все жанры при наличии роли USER")
    void getGenreList_WithUserRole_ShouldReturnGenres() throws Exception {
        given(genreService.findAll()).willReturn(sampleGenres);

        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'name':'Genre 1'},{'id':2,'name':'Genre 2'}]"));
    }

    @Test
    @DisplayName("Должен возвращать 401 при доступе к списку жанров без аутентификации")
    void getGenreList_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен отобразить все жанры при наличии роли ADMIN")
    void getGenreList_WithAdminRole_ShouldReturnGenres() throws Exception {
        given(genreService.findAll()).willReturn(sampleGenres);

        mockMvc.perform(get("/api/genres"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'name':'Genre 1'},{'id':2,'name':'Genre 2'}]"));
    }
}

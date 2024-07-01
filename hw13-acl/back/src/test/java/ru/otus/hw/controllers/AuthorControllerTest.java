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
import ru.otus.hw.controlles.AuthorController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.services.AuthorServiceImpl;

import java.util.Arrays;
import java.util.List;

@DisplayName("Тесты контроллера связанного с авторами.")
@WebMvcTest(AuthorController.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorServiceImpl authorService;

    private List<AuthorDTO> sampleAuthors;

    @BeforeEach
    void setUp() {
        sampleAuthors = Arrays.asList(
            new AuthorDTO(1L, "Author_1"),
            new AuthorDTO(2L, "Author_2"),
            new AuthorDTO(3L, "Author_3")
        );
    }

    @Test
    @WithMockUser(
        username = "user",
        authorities = {"ROLE_USER"}
    )
    @DisplayName("Должен отобразить всех авторов при наличии роли USER")
    void getAuthorList_WithUserRole_ShouldReturnAuthors() throws Exception {
        given(authorService.findAll()).willReturn(sampleAuthors);

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'fullName':'Author_1'},{'id':2,'fullName':'Author_2'},{'id':3,'fullName':'Author_3'}]"));
    }

    @Test
    @DisplayName("Должен возвращать 401 при доступе к списку авторов без аутентификации")
    void getAuthorList_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(
        username = "admin",
        authorities = {"ROLE_ADMIN"}
    )
    @DisplayName("Должен отобразить всех авторов при наличии роли ADMIN")
    void getAuthorList_WithAdminRole_ShouldReturnAuthors() throws Exception {
        given(authorService.findAll()).willReturn(sampleAuthors);

        mockMvc.perform(get("/api/authors"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'fullName':'Author_1'},{'id':2,'fullName':'Author_2'},{'id':3,'fullName':'Author_3'}]"));
    }
}

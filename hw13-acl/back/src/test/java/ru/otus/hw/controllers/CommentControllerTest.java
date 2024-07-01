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
import ru.otus.hw.controlles.CommentController;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.CommentCreateDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.dtos.CommentEditDTO;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebMvcTest(CommentController.class)
@DisplayName("Тесты контроллера связанного с комментариями.")
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentServiceImpl commentService;

    private CommentDTO sampleComment;
    private List<CommentDTO> sampleComments;

    @BeforeEach
    void setUp() {
        BookDTO sampleBook = new BookDTO(1L, "Sample Book", null, null);
        sampleComment = new CommentDTO(1L, "Sample comment", sampleBook);
        sampleComments = Arrays.asList(
            new CommentDTO(1L, "Comment 1", sampleBook),
            new CommentDTO(2L, "Comment 2", sampleBook)
        );
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен отобразить все комментарии книги")
    void getCommentBookList_WithUserRole_ShouldReturnComments() throws Exception {
        given(commentService.findAllByBookId(1L)).willReturn(sampleComments);

        mockMvc.perform(get("/api/books/1/comments"))
            .andExpect(status().isOk())
            .andExpect(content().json("[{'id':1,'text':'Comment 1','book':{'id':1,'title':'Sample Book'}},{'id':2,'text':'Comment 2','book':{'id':1,'title':'Sample Book'}}]"));
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен отобразить комментарий по ID")
    void getCommentById_WithUserRole_ShouldReturnComment() throws Exception {
        given(commentService.findById(1L)).willReturn(Optional.of(sampleComment));

        mockMvc.perform(get("/api/comments/1"))
            .andExpect(status().isOk())
            .andExpect(content().json("{'id':1,'text':'Sample comment','book':{'id':1,'title':'Sample Book'}}"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен создать новый комментарий с ролью ADMIN")
    void addCommentForBook_WithAdminRole_ShouldCreateComment() throws Exception {
        CommentCreateDTO newComment = new CommentCreateDTO( "New comment", 1L);
        given(commentService.insert(newComment)).willReturn(sampleComment);

        mockMvc.perform(post("/api/comments")
                .with(csrf())
                .contentType("application/json")
                .content("{\"bookId\":1,\"text\":\"New comment\"}"))
            .andExpect(status().isCreated())
            .andExpect(content().json("{\"id\":1,\"text\":\"Sample comment\",\"book\":{\"id\":1,\"title\":\"Sample Book\"}}"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен обновить комментарий с ролью ADMIN")
    void updateComment_WithAdminRole_ShouldUpdateComment() throws Exception {
        CommentEditDTO editedComment = new CommentEditDTO(1L, "Updated comment", 1L);
        given(commentService.update(editedComment)).willReturn(sampleComment);

        mockMvc.perform(patch("/api/comments/1")
                .with(csrf())
                .contentType("application/json")
                .content("{\"text\":\"Updated comment\", \"bookId\":1}"))
            .andExpect(status().isAccepted())
            .andExpect(content().json("{\"id\":1,\"text\":\"Sample comment\",\"book\":{\"id\":1,\"title\":\"Sample Book\"}}"));
    }

    @Test
    @WithMockUser(username = "admin", authorities = {"ROLE_ADMIN"})
    @DisplayName("Должен удалить комментарий с ролью ADMIN")
    void deleteComment_WithAdminRole_ShouldDeleteComment() throws Exception {
        mockMvc.perform(delete("/api/comments/1") .with(csrf()))
            .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Должен возвращать 401 при доступе к комментариям без аутентификации")
    void getCommentBookList_WithoutAuthentication_ShouldReturn401() throws Exception {
        mockMvc.perform(get("/api/books/1/comments"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен возвращать 403 при создании комментария с ролью USER")
    void addCommentForBook_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(post("/api/comments")
                .contentType("application/json")
                .content("{\"bookId\":1,\"text\":\"New comment\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен возвращать 403 при обновлении комментария с ролью USER")
    void updateComment_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(patch("/api/comments/1")
                .contentType("application/json")
                .content("{\"text\":\"Updated comment\"}"))
            .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    @DisplayName("Должен возвращать 403 при удалении комментария с ролью USER")
    void deleteComment_WithUserRole_ShouldReturn403() throws Exception {
        mockMvc.perform(delete("/api/comments/1"))
            .andExpect(status().isForbidden());
    }
}

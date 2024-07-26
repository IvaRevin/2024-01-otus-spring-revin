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
import ru.otus.hw.dtos.CommentCreateDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.dtos.CommentEditDTO;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @CircuitBreaker(name = "commentBreaker", fallbackMethod = "unknownCommentListFallback")
    @GetMapping("/api/books/{id}/comments")
    public List<CommentDTO> getCommentBookList(@PathVariable("id") long id) {
        return commentService.findAllByBookId(id);
    }

    @CircuitBreaker(name = "commentBreaker", fallbackMethod = "unknownCommentFallback")
    @GetMapping("/api/comments/{id}")
    public CommentDTO getCommentById(@PathVariable("id") Long id) {
        return commentService.findById(id).orElse(null);
    }

    @PostMapping("/api/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDTO addCommentForBook(@Valid @RequestBody CommentCreateDTO dto) {
        return commentService.insert(dto);
    }

    @PatchMapping("/api/comments/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public CommentDTO updateComment(@PathVariable("id") Long id,
                                    @Valid @RequestBody CommentEditDTO dto) {
        dto.setId(id);
        return commentService.update(dto);
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("id") Long id) {
        commentService.deleteById(id);
    }

    public CommentDTO unknownCommentFallback(Exception ex) {
        log.error(ex.getMessage(), ex);
        return new CommentDTO(null, "NaN", null);
    }

    public List<CommentDTO> unknownCommentListFallback(Exception ex) {
        log.error("List book error:" + ex.getMessage(), ex);
        return Collections.emptyList();
    }
}

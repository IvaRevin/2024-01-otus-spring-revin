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
import ru.otus.hw.dtos.CommentCreateDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.dtos.CommentEditDTO;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    @GetMapping("/api/books/{id}/comments")
    public Flux<CommentDTO> getCommentBookList(@PathVariable("id") String id) {
        return this.commentRepository.findAllByBookId(id)
            .map(CommentDTO::commentToDto);
    }

    @GetMapping("/api/comments/{id}")
    public Mono<ResponseEntity<CommentDTO>> getCommentById(@PathVariable("id") String id) {
        return this.commentRepository.findById(id)
            .map(CommentDTO::commentToDto)
            .map(ResponseEntity::ok)
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PostMapping("/api/comments")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Mono<ResponseEntity<CommentDTO>> addCommentForBook(@Valid @RequestBody CommentCreateDTO dto) {
        return this.bookRepository.findById(dto.getBookId())
            .flatMap(book -> this.commentRepository.save(
                Comment.builder()
                    .book(book)
                    .text(dto.getText())
                    .build()
            )).map(CommentDTO::commentToDto)
            .map(newComment -> new ResponseEntity<>(newComment, HttpStatus.ACCEPTED))
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @PatchMapping("/api/comments/{id}")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public Mono<ResponseEntity<CommentDTO>> updateComment(@PathVariable("id") String id,
                                    @Valid @RequestBody CommentEditDTO dto) {
        dto.setId(id);
        return this.bookRepository.findById(dto.getBookId())
            .flatMap(book -> this.commentRepository.save(
                Comment.builder()
                    .id(dto.getId())
                    .book(book)
                    .text(dto.getText())
                    .build()
            )).map(CommentDTO::commentToDto)
            .map(newComment -> new ResponseEntity<>(newComment, HttpStatus.ACCEPTED))
            .switchIfEmpty(Mono.fromCallable(() -> ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/api/comments/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Mono<ResponseEntity<Void>> deleteComment(@PathVariable("id") String id) {
        return this.commentRepository.deleteById(id)
            .then(Mono.fromCallable(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT)));
    }
}

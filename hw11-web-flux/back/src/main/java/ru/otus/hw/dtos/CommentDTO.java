package ru.otus.hw.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.models.Comment;

@Data
@SuperBuilder
public class CommentDTO {

    private String id;

    private String text;

    private BookDTO book;

    public static CommentDTO commentToDto(Comment comment) {

        return CommentDTO.builder()
            .id(comment.getId())
            .text(comment.getText())
            .book(BookDTO.bookToDto(comment.getBook()))
            .build();
    }
}

package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentCreateDTO {

    private String text;

    private Long bookId;
}

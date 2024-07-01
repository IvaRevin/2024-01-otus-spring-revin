package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentEditDTO {

    private Long id;

    private String text;

    private Long bookId;
}

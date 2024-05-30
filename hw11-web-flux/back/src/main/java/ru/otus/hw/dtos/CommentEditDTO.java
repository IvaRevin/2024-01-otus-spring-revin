package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentEditDTO {

    private String id;

    private String text;

    private String bookId;
}

package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCreateDTO {

    private String id;

    private String title;

    private String authorId;

    private String[] genreIds;
}

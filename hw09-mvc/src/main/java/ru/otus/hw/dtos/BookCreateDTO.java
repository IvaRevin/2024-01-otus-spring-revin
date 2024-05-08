package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookCreateDTO {

    private Long id;

    private String title;

    private Long authorId;

    private Long[] genreIds;
}

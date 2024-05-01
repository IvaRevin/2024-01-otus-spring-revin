package ru.otus.hw.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.models.Genre;

@Data
@SuperBuilder
public class GenreDTO {
    private String id;

    private String name;

    public static GenreDTO genreToDto(Genre genre) {

        return GenreDTO.builder()
            .id(genre.getId())
            .name(genre.getName())
            .build();
    }
}

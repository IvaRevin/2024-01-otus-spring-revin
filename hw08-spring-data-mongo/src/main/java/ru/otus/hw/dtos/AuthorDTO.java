package ru.otus.hw.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.models.Author;

@Data
@SuperBuilder
public class AuthorDTO {

    private String id;

    private String fullName;

    public static AuthorDTO authorToDto(Author author) {

        return AuthorDTO.builder()
            .id(author.getId())
            .fullName(author.getFullName())
            .build();
    }
}

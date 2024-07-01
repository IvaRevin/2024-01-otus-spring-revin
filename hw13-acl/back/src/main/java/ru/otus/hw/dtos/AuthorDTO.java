package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.models.Author;

@Data
@SuperBuilder
@AllArgsConstructor
public class AuthorDTO {

    private Long id;

    private String fullName;

    public static AuthorDTO authorToDto(Author author) {

        return AuthorDTO.builder()
            .id(author.getId())
            .fullName(author.getFullName())
            .build();
    }
}

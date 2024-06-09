package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BookEditDTO {

    private String id;

    private String title;

    private String authorId;

    private String[] genreIds;

    public static BookEditDTO fromBookDto(BookDTO bookDto) {
        return new BookEditDTO(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor() == null ? null : bookDto.getAuthor().getId(),
            bookDto.getGenres() == null
                ? null : bookDto.getGenres().stream()
                                .map(GenreDTO::getId)
                                .toList()
                                 .toArray(new String[0])
        );
    }
}

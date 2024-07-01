package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BookEditDTO {

    private Long id;

    private String title;

    private Long authorId;

    private Long[] genreIds;

    public static BookEditDTO fromBookDto(BookDTO bookDto) {
        return new BookEditDTO(
            bookDto.getId(),
            bookDto.getTitle(),
            bookDto.getAuthor() == null ? null : bookDto.getAuthor().getId(),
            bookDto.getGenres() == null
                ? null : bookDto.getGenres().stream()
                                .map(GenreDTO::getId)
                                .toList()
                                 .toArray(new Long[0])
        );
    }
}

package ru.otus.hw.dtos;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.models.Book;

import java.util.List;
import java.util.stream.Collectors;

@Data
@SuperBuilder
public class BookDTO {

    private long id;

    private String title;

    private AuthorDTO author;

    private List<GenreDTO> genres;

    public static BookDTO bookToDto(Book book) {

        return BookDTO.builder()
            .id(book.getId())
            .title(book.getTitle())
            .author(AuthorDTO.authorToDto(book.getAuthor()))
            .genres(book.getGenres().stream().map(GenreDTO::genreToDto).collect(Collectors.toList()))
            .build();
    }
}

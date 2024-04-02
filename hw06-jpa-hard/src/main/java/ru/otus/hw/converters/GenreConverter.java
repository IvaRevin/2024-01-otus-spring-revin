package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.GenreDTO;

@Component
public class GenreConverter {
    public String genreToString(GenreDTO genre) {
        return "Id: %d, Name: %s".formatted(genre.getId(), genre.getName());
    }
}

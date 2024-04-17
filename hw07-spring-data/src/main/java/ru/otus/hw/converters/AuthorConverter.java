package ru.otus.hw.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.AuthorDTO;

@Component
public class AuthorConverter {
    public String authorToString(AuthorDTO author) {
        return "Id: %d, FullName: %s".formatted(author.getId(), author.getFullName());
    }
}

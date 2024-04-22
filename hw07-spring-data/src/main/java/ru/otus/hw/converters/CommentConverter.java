package ru.otus.hw.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.CommentDTO;

@RequiredArgsConstructor
@Component
public class CommentConverter {

    private final BookConverter bookConverter;

    public String commentToString(CommentDTO comment) {
        return "Id: %d, text: %s, book: {%s}".formatted(
            comment.getId(),
            comment.getText(),
            bookConverter.bookToString(comment.getBook())
        );
    }
}

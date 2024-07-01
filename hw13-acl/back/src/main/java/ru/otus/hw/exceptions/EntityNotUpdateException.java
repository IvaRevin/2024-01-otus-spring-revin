package ru.otus.hw.exceptions;

import ru.otus.hw.models.Book;

public class EntityNotUpdateException extends RuntimeException {
    public EntityNotUpdateException(Book book) {
        super(String.format("Error while update book: %s", book.getTitle()));
    }
}

package ru.otus.hw.exceptions;

public class EmailSenderException extends RuntimeException {
    public EmailSenderException(String message,  Throwable ex) {
        super(message, ex);
    }
}

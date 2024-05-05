package ru.otus.hw.services;

import ru.otus.hw.dtos.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDTO> findById(long id);

    List<CommentDTO> findAllByBookId(long bookId);

    CommentDTO insert(String text, long bookId);

    CommentDTO update(long id, String text, long bookId);

    void deleteById(long id);
}

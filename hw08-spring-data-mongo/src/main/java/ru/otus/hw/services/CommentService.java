package ru.otus.hw.services;

import ru.otus.hw.dtos.CommentDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDTO> findById(String id);

    List<CommentDTO> findAllByBookId(String bookId);

    CommentDTO insert(String text, String bookId);

    CommentDTO update(String id, String text, String bookId);

    void deleteById(String id);
}

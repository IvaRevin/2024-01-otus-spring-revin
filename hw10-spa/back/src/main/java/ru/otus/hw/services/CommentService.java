package ru.otus.hw.services;

import ru.otus.hw.dtos.CommentCreateDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.dtos.CommentEditDTO;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    Optional<CommentDTO> findById(long id);

    List<CommentDTO> findAllByBookId(long bookId);

    CommentDTO insert(CommentCreateDTO dto);

    CommentDTO update(CommentEditDTO dto);

    void deleteById(long id);
}

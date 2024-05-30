package ru.otus.hw.services;

import ru.otus.hw.dtos.BookDTO;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookService {
    Optional<BookDTO> findById(String id);

    List<BookDTO> findAll();

    BookDTO insert(String title, String authorId, Set<String> genresIds);

    BookDTO update(String id, String title, String authorId, Set<String> genresIds);

    void deleteById(String id);
}

package ru.otus.hw.services;

import ru.otus.hw.dtos.BookCreateDTO;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.BookEditDTO;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDTO> findById(long id);

    List<BookDTO> findAll();

    BookDTO create(BookCreateDTO bookDTO);

    BookDTO update(BookEditDTO bookDTO);

    void deleteById(long id);
}

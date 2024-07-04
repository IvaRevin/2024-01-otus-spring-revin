package ru.otus.hw.services;

import ru.otus.hw.dtos.AuthorDTO;

import java.util.List;

public interface AuthorService {
    List<AuthorDTO> findAll();
}

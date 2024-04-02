package ru.otus.hw.services;

import ru.otus.hw.dtos.GenreDTO;

import java.util.List;

public interface GenreService {
    List<GenreDTO> findAll();
}

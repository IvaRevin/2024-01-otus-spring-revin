package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    @Transactional(readOnly = true)
    public List<GenreDTO> findAll() {
        return genreRepository.findAll().stream()
            .map(GenreDTO::genreToDto)
            .collect(Collectors.toList());
    }

    public GenreDTO findById(Long id) {
        return genreRepository.findById(id).map(GenreDTO::genreToDto).orElse(null);
    }
}

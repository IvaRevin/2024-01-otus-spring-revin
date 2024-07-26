package ru.otus.hw.controlles;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@CircuitBreaker(name = "genreBreaker", fallbackMethod = "unknownGenreListFallback")
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl genreService;

    @GetMapping("/api/genres")
    public List<GenreDTO> getGenreList() {
        return genreService.findAll();
    }

    public List<GenreDTO> unknownGenreListFallback(Exception ex) {
        log.error(ex.getMessage());
        return Collections.emptyList();
    }
}

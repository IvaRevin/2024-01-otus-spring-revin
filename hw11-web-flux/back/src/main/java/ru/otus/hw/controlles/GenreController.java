package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.repositories.GenreRepository;

@RestController
@RequiredArgsConstructor
public class GenreController {

    private final GenreRepository genreRepository;

    @GetMapping("/api/genres")
    public Flux<GenreDTO> getGenreList() {
        return this.genreRepository.findAll()
            .map(GenreDTO::genreToDto);
    }
}

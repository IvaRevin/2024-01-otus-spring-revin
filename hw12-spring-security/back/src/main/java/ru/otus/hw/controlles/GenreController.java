package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GenreController {

    private final GenreServiceImpl genreService;

    @GetMapping("/genres")
    public List<GenreDTO> getGenreList() {
        return genreService.findAll();
    }
}

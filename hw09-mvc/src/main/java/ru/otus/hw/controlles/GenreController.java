package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.hw.dtos.GenreDTO;
import ru.otus.hw.services.GenreServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class GenreController {

    private final GenreServiceImpl genreService;

    @GetMapping("/genres")
    public String getGenreList(Model model) {
        List<GenreDTO> genreDTOList = genreService.findAll();
        model.addAttribute("genres", genreDTOList);
        return "genres/genres";
    }
}

package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.services.AuthorServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping("/authors")
    public String getAuthorList(Model model) {
        List<AuthorDTO> authorDTOList = authorService.findAll();
        model.addAttribute("authors", authorDTOList);
        return "authors/authors";
    }
}

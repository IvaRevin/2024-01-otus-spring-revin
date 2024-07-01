package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.services.AuthorServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping("/authors")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public List<AuthorDTO> getAuthorList() {
        return authorService.findAll();
    }
}

package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.repositories.AuthorRepository;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorRepository authorRepository;

    @GetMapping("/api/authors")
    public Flux<AuthorDTO> getAuthorList() {
        return authorRepository.findAll()
            .map(AuthorDTO::authorToDto);
    }
}

package ru.otus.hw.controlles;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.dtos.AuthorDTO;
import ru.otus.hw.services.AuthorServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@CircuitBreaker(name = "authorBreaker", fallbackMethod = "unknownAuthorListFallback")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorServiceImpl authorService;

    @GetMapping("/api/authors")
    public List<AuthorDTO> getAuthorList() {
        return authorService.findAll();
    }

    public List<AuthorDTO> unknownAuthorListFallback(Exception ex) {
        log.error(ex.getMessage());
        return Collections.emptyList();
    }
}

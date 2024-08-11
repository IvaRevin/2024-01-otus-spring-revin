package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.config.commons.VersionFileNameProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1")
@Slf4j
public class VersionController {

    private final VersionFileNameProvider versionFileNameProvider;

    @GetMapping("/version")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<String> getVersion() {

        var reader = new BufferedReader(
            new InputStreamReader(
                Objects.requireNonNull(
                    this.getClass().getClassLoader().getResourceAsStream(versionFileNameProvider.getVersionFileName())
                ),
                StandardCharsets.UTF_8
            )
        );
        try (reader) {

            return new ResponseEntity<>(reader.readLine(), HttpStatus.OK);
        } catch (IOException e) {

            VersionController.log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}

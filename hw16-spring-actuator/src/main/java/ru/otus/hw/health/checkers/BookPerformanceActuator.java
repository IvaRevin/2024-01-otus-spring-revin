package ru.otus.hw.health.checkers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookPerformanceActuator implements HealthIndicator {

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        long startTime = System.currentTimeMillis();
        try {
            bookRepository.findAll();
            long duration = System.currentTimeMillis() - startTime;
            return Health.up()
                .withDetail("responseTime", duration + "ms")
                .build();
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;
            return Health.down()
                .withDetail("responseTime", duration + "ms")
                .withDetail("error", ex.getMessage())
                .build();
        }
    }
}

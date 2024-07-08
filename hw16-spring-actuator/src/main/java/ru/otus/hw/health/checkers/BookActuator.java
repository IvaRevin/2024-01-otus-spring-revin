package ru.otus.hw.health.checkers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.repositories.BookRepository;

@Component
@RequiredArgsConstructor
public class BookActuator implements HealthIndicator {

    private static final long MIN_BOOK_COUNT = 0;

    private final BookRepository bookRepository;

    @Override
    public Health health() {
        try {
            long count = bookRepository.count();
            if (count < MIN_BOOK_COUNT) {
                return Health.down()
                    .withDetail("message", "No books ")
                    .build();
            } else {
                return Health.up()
                    .withDetail("message", "Book count: " + count)
                    .build();
            }
        } catch (Exception ex) {
            return Health.down()
                .withDetail("error", ex.getMessage())
                .build();
        }
    }
}

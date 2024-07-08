package ru.otus.hw.health.checkers;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
@RequiredArgsConstructor
public class DbActuator implements HealthIndicator {

    private final DataSource dataSource;

    @Override
    public Health health() {
        try {
            Connection connection = dataSource.getConnection();
            return Health.up()
                .withDetail("message", "Database: " + connection.getCatalog() + " is up")
                .build();
        } catch (Exception e) {
            return Health.down(e)
                .withDetail("message", "Database is down")
                .build();
        }
    }
}

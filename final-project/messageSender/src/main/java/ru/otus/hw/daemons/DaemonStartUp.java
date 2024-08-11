package ru.otus.hw.daemons;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@AllArgsConstructor
@Slf4j
public class DaemonStartUp implements StartUp {

    private final Set<Daemon> daemons;

    @PostConstruct
    public void onStart() {
        log.info("DaemonStartUp initialized at {}", Instant.now());
    }

    @PreDestroy
    public void onStop() {
        log.info("DaemonStartUp is stopping at {}", Instant.now());
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void executeDaemons() {
        for (Daemon daemon : this.daemons) {
            if (daemon.isEnabled()) {
                try {
                    daemon.action();
                    log.info("Executed daemon: {}", daemon);
                } catch (Exception e) {
                    log.error("Error executing daemon: {}", daemon, e);
                }
            }
        }
    }
}

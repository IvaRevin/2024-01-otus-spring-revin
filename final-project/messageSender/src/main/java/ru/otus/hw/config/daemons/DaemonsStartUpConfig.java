package ru.otus.hw.config.daemons;

import java.time.Duration;

public interface DaemonsStartUpConfig {

    Duration getDaemonInterval();

    Duration getDaemonDelay();
}

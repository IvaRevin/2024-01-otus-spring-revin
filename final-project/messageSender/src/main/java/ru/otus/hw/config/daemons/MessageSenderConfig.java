package ru.otus.hw.config.daemons;

import java.time.Duration;

public interface MessageSenderConfig {

    Duration getMessageSenderDaemonInterval();

    boolean isMessageSenderDaemonEnabled();

    Duration getMessageSenderDaemonDelay();

    int getMaximumPoolSizeForEdiDocumentGroupTaskDaemon();

    Duration getThrottlingTaskDuration();
}

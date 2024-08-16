package ru.otus.hw.models;

import java.time.Instant;

public interface ImmutableThrottlingTask {

    Instant getLastErrorDate();

    boolean isSkipping();

    ImmutableThrottlingTask updateLastErrorDate();
}

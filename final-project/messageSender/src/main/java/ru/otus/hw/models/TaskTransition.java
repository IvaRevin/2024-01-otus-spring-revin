package ru.otus.hw.models;

public interface TaskTransition<T extends ImmutableThrottlingTask> {

    void transit(T task);
}

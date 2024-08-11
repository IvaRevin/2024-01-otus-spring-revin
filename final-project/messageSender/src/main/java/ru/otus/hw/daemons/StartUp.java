package ru.otus.hw.daemons;

public interface StartUp {

    void onStart();

    void onStop();

    default int getPriority() {

        return 1;
    }
}

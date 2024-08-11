package ru.otus.hw.services;

import ru.otus.hw.models.MessageSenderTask;

public interface MessageSenderService {

    void sendMessage(MessageSenderTask task);
}

package ru.otus.hw.services.factory;

import ru.otus.hw.models.MessageType;
import ru.otus.hw.services.MessageSenderService;

import java.util.Optional;

public interface MessageSenderServiceFactory {

    Optional<MessageSenderService> getMessageSenderService(MessageType templateType);
}

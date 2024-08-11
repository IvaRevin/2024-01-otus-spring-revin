package ru.otus.hw.services.task;

import ru.otus.hw.dtos.MessageSenderTaskDTO;
import ru.otus.hw.models.MessageSenderTask;

public interface MessageSenderTaskService {

    void createTask(MessageSenderTaskDTO dto);

    void updateTask(MessageSenderTask task);
}

package ru.otus.hw.transition.tasks;

import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.models.TaskTransition;

public interface MessageTaskTransition extends TaskTransition<MessageSenderTask> {

    MessageState getState();
}

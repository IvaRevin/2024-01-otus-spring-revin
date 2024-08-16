package ru.otus.hw.transition.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.services.task.MessageSenderTaskService;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageTaskErrorTransition implements MessageTaskTransition {

    private final MessageSenderTaskService messageSenderTaskService;

    @Override
    public void transit(MessageSenderTask task) {

        MessageSenderTask updatedTask = task.toBuilder()
            .state(MessageState.READY_TO_SEND)
            .lastErrorDate(null)
            .build();

        messageSenderTaskService.updateTask(updatedTask);
    }

    @Override
    public MessageState getState() {
        return MessageState.ERROR;
    }
}

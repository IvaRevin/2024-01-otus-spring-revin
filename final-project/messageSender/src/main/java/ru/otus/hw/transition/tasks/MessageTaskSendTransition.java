package ru.otus.hw.transition.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.services.task.MessageSenderTaskService;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageTaskSendTransition implements MessageTaskTransition {

    private final MessageSenderTaskService messageSenderTaskService;

    @Override
    public void transit(MessageSenderTask task) {

        MessageSenderTask updatedTask = task.toBuilder()
            .sendDate(Instant.now())
            .build();

        messageSenderTaskService.updateTask(updatedTask);

        MessageTaskSendTransition.log.debug("Sending task with id: " + task.getId() + " finished successfully");
    }

    @Override
    public MessageState getState() {
        return MessageState.SEND;
    }
}

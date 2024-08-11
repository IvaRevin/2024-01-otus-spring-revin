package ru.otus.hw.transition.tasks;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.services.MessageSenderService;
import ru.otus.hw.services.factory.MessageSenderServiceFactory;
import ru.otus.hw.services.task.MessageSenderTaskService;

import java.time.Instant;

@Component
@Slf4j
@RequiredArgsConstructor
public class MessageTaskReadyToSendTransition implements MessageTaskTransition {

    private final MessageSenderTaskService messageSenderTaskService;

    private final MessageSenderServiceFactory messageSenderServiceFactory;

    @Override
    public void transit(MessageSenderTask task) {
        try {

            var messageSenderService = this.getMessageSenderService(task);
            messageSenderService.sendMessage(task);

            MessageSenderTask updatedTask = task.toBuilder()
                .readyDate(Instant.now())
                .state(MessageState.SEND)
                .build();

            messageSenderTaskService.updateTask(updatedTask);
        } catch (Exception e) {

            log.error("Error while sending message task with id: " + task.getId(), e);

            MessageSenderTask updatedTask = task.toBuilder()
                .lastErrorDate(Instant.now())
                .state(MessageState.ERROR)
                .build();

            messageSenderTaskService.updateTask(updatedTask);
        }
    }

    @Override
    public MessageState getState() {
        return MessageState.READY_TO_SEND;
    }

    private MessageSenderService getMessageSenderService(MessageSenderTask task) {

        return this.messageSenderServiceFactory
            .getMessageSenderService(task.getMessageType())
            .orElseThrow(() ->
                new RuntimeException("No service found for template type: " + task.getMessageType())
            );
    }
}

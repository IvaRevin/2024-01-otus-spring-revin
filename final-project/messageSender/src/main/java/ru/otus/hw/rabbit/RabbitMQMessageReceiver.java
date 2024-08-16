package ru.otus.hw.rabbit;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.MessageSenderTaskDTO;
import ru.otus.hw.services.task.MessageSenderTaskService;

@Component
public class RabbitMQMessageReceiver {

    private final MessageSenderTaskService messageSenderTaskService;

    private final NotificationSender notificationSender;

    public RabbitMQMessageReceiver(
        MessageSenderTaskService messageSenderTaskService,
        NotificationSender notificationSender
    ) {
        this.messageSenderTaskService = messageSenderTaskService;
        this.notificationSender = notificationSender;
    }

    @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public void receiveTask(MessageSenderTaskDTO task) {
        try {
            messageSenderTaskService.createTask(task);
            notificationSender.sendSuccessNotification(task);
        } catch (Exception e) {
            notificationSender.sendErrorNotification(task, e.getMessage());
        }
    }
}

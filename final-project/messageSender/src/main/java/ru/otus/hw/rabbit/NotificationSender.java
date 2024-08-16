package ru.otus.hw.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.otus.hw.dtos.MessageSenderTaskDTO;

@Component
public class NotificationSender {

    private final RabbitTemplate rabbitTemplate;

    public NotificationSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendSuccessNotification(MessageSenderTaskDTO task) {
        String successMessage = "Task successfully processed: " + task.toString();
        rabbitTemplate.convertAndSend(RabbitMqConfig.SUCCESS_QUEUE, successMessage);
    }

    public void sendErrorNotification(MessageSenderTaskDTO task, String errorMessage) {
        String errorNotification = "Error processing task: " + task.toString() + ". Error: " + errorMessage;
        rabbitTemplate.convertAndSend(RabbitMqConfig.ERROR_QUEUE, errorNotification);
    }
}

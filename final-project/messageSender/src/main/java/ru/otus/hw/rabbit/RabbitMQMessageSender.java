package ru.otus.hw.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import ru.otus.hw.dtos.MessageSenderTaskDTO;

@Service
public class RabbitMQMessageSender {

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendTask(MessageSenderTaskDTO task) {
        rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, task);
    }
}

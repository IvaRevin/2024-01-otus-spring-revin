package ru.otus.hw.rabbit;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_NAME = "messageTasksQueue";

    public static final String EXCHANGE_NAME = "messageTasksExchange";

    public static final String ROUTING_KEY = "messageTasks";

    public static final String SUCCESS_QUEUE = "taskSuccessQueue";

    public static final String ERROR_QUEUE = "taskErrorQueue";

    @Bean
    public Queue taskQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    @Bean
    public Queue successQueue() {
        return new Queue(SUCCESS_QUEUE, false);
    }

    @Bean
    public Queue errorQueue() {
        return new Queue(ERROR_QUEUE, false);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding taskBinding(Queue taskQueue, TopicExchange exchange) {
        return BindingBuilder.bind(taskQueue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding successBinding(Queue successQueue, TopicExchange exchange) {
        return BindingBuilder.bind(successQueue).to(exchange).with(SUCCESS_QUEUE);
    }

    @Bean
    public Binding errorBinding(Queue errorQueue, TopicExchange exchange) {
        return BindingBuilder.bind(errorQueue).to(exchange).with(ERROR_QUEUE);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        template.setExchange(EXCHANGE_NAME);
        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

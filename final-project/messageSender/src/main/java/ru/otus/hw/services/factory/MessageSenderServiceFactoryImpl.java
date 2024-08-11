package ru.otus.hw.services.factory;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.hw.models.MessageType;
import ru.otus.hw.services.MessageSenderService;
import ru.otus.hw.utils.MessageServiceType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MessageSenderServiceFactoryImpl implements MessageSenderServiceFactory {

    private final Map<String, MessageSenderService> serviceMap = new HashMap<>();


    public MessageSenderServiceFactoryImpl(List<MessageSenderService> services) {
        for (MessageSenderService service : services) {
            MessageServiceType annotation = service.getClass().getAnnotation(MessageServiceType.class);
            if (annotation != null) {
                serviceMap.put(annotation.value().name(), service);
            }
        }
    }

    @Override
    public Optional<MessageSenderService> getMessageSenderService(MessageType templateType) {
        return Optional.ofNullable(serviceMap.get(templateType.name()));
    }
}

package ru.otus.hw.services.task;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.MessageSenderTaskDTO;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.models.MessageTemplateType;
import ru.otus.hw.models.MessageType;
import ru.otus.hw.repositories.MessageSenderTaskRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageSenderTaskServiceImpl implements MessageSenderTaskService {

    private final MessageSenderTaskRepository messageSenderTaskRepository;

    @Transactional
    @Override
    public void createTask(MessageSenderTaskDTO dto) {

        MessageSenderTask newTask = MessageSenderTask.builder()
            .id(UUID.randomUUID())
            .state(MessageState.CREATED)
            .messageTemplateType(MessageTemplateType.valueOf(dto.getMessageTemplateType()))
            .messageTemplateFields(dto.getMessageFields())
            .messageAddress(dto.getMessageAddress())
            .messageType(MessageType.valueOf(dto.getMessageType()))
            .build();

        this.messageSenderTaskRepository.save(newTask);
    }

    @Transactional
    @Override
    public void updateTask(MessageSenderTask task) {

        this.messageSenderTaskRepository.save(task);
    }

}

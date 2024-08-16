package ru.otus.hw.daemons;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.daemons.MessageSenderConfig;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageState;
import ru.otus.hw.transition.tasks.MessageTaskTransition;
import ru.otus.hw.models.TaskTransition;
import ru.otus.hw.repositories.MessageSenderTaskRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
@Slf4j
public class SendMessageDaemon extends AbstractTaskDaemon<MessageSenderTask> {

    private static final int CORE_POOL_SIZE = 1;

    private static final int KEEP_ALIVE_TIME = 1;

    private final MessageSenderConfig messageSenderConfig;

    private final MessageSenderTaskRepository messageSenderTaskRepository;

    private final Map<MessageState, MessageTaskTransition> transitions;

    protected SendMessageDaemon(
        MessageSenderConfig messageSenderConfig,
        MessageSenderTaskRepository messageSenderTaskRepository,
        Set<MessageTaskTransition> transitions
    ) {
        super(
            MessageSenderTask.class,
            "SendMessageDaemonThread-%s",
            SendMessageDaemon.CORE_POOL_SIZE,
            messageSenderConfig.getMaximumPoolSizeForEdiDocumentGroupTaskDaemon(),
            SendMessageDaemon.KEEP_ALIVE_TIME
        );

        this.messageSenderConfig = messageSenderConfig;
        this.messageSenderTaskRepository = messageSenderTaskRepository;
        this.transitions = transitions.stream()
            .collect(Collectors.toMap(MessageTaskTransition::getState, Function.identity()));
    }


    @Override
    public String getDaemonName() {
        return "SendMessageDaemon";
    }

    @Override
    protected List<UUID> getTaskIds() {
        return this.messageSenderTaskRepository
            .findTasksToStartSend(Instant.now().minus(this.messageSenderConfig.getThrottlingTaskDuration()))
            .stream()
            .map(MessageSenderTask::getId)
            .collect(Collectors.toList());
    }

    @Override
    protected MessageSenderTask getTask(UUID taskId) {
        return this.messageSenderTaskRepository.findById(taskId).orElse(null);
    }

    @Override
    protected void updateTask(MessageSenderTask task) {

        this.messageSenderTaskRepository.save(task);
    }

    @Override
    protected Optional<TaskTransition<MessageSenderTask>> getTransition(MessageSenderTask task) {
        return Optional.ofNullable(this.transitions.get(task.getState()));
    }

    @Override
    public boolean isEnabled() {
        return this.messageSenderConfig.isMessageSenderDaemonEnabled();
    }

    @Override
    public Duration getInitialDelay() {
        return Duration.ofNanos(this.messageSenderConfig.getMessageSenderDaemonDelay().toNanos());
    }

    @Override
    public Duration getInterval() {
        return Duration.ofNanos(this.messageSenderConfig.getMessageSenderDaemonInterval().toNanos());
    }
}

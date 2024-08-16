package ru.otus.hw.daemons;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.models.ImmutableThrottlingTask;
import ru.otus.hw.models.TaskTransition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public abstract class AbstractTaskDaemon<T extends ImmutableThrottlingTask> implements Daemon {

    private static final int QUEUE_CAPACITY = 50;

    private final Class<T> taskClass;

    private final ExecutorService transitionsPool;

    private final ConcurrentSkipListSet<UUID> queuedTaskIds;

    protected AbstractTaskDaemon (
        Class<T> taskClass,
        String threadNameFormat,
        int corePoolSize,
        int maximumPoolSize,
        long keepAliveTime
    ) {
        this.taskClass = taskClass;

        final var threadFactory = getThreadFactory(threadNameFormat);

        this.transitionsPool = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(QUEUE_CAPACITY),
            threadFactory
        );

        this.queuedTaskIds = new ConcurrentSkipListSet<>();
    }

    public abstract String getDaemonName();

    @Override
    public final void action() {

        AbstractTaskDaemon.log.debug("Started");

        try {
            this.processAction();
        } catch (Exception e) {
            AbstractTaskDaemon.log.error("Unknown error occurred", e);
        }
    }

    private void processAction() {
        List<UUID> taskIds = this.getTaskIds();

        int queuedCount = 0;
        int alreadyQueuedCount = 0;

        for (UUID taskId : taskIds) {
            if (this.queuedTaskIds.contains(taskId)) {
                alreadyQueuedCount += 1;
                continue;
            }

            this.transitionsPool.submit(() -> this.transit(taskId));
            this.queuedTaskIds.add(taskId);
            queuedCount += 1;
        }

        var message = String.format(
            "Finished. Extracted: %s, Queued: %s, Already queued: %s",
            taskIds.size(),
            queuedCount,
            alreadyQueuedCount
        );
        AbstractTaskDaemon.log.debug(message);
    }

    protected abstract List<UUID> getTaskIds();

    protected final void transit(UUID taskId) {

        try {

            AbstractTaskDaemon.log.debug(taskId.toString(), "Transition started");

            this.makeTransit(taskId);

            AbstractTaskDaemon.log.debug(taskId.toString(), "Transition finished");
        } catch (OutOfMemoryError e) {

            AbstractTaskDaemon.log.error(taskId.toString(), "Transition failed because of out of memory", e);
            throw e;
        } catch (Exception e) {

            AbstractTaskDaemon.log.error(taskId.toString(), "Transition failed because of some error", e);
        } finally {

            this.queuedTaskIds.remove(taskId);
        }
    }

    private void makeTransit(UUID taskId) {

        var task = this.getTask(taskId);
        TaskTransition<T> transition = this.getTransition(task).orElse(null);

        if (transition == null) {

            this.updateLastErrorDate(task);
            throw new IllegalStateException("Transition not found");
        }

        try {

            transition.transit(task);
        } catch (Exception e) {

            this.updateLastErrorDate(task);
            throw e;
        }
    }

    protected abstract T getTask(UUID taskId);

    protected abstract void updateTask(T task);

    protected abstract Optional<TaskTransition<T>> getTransition(T task);

    private void updateLastErrorDate(T task) {

        try {

            T updatedTask = this.toT(task.updateLastErrorDate());
            this.updateTask(updatedTask);
        } catch (Exception innerError) {

            AbstractTaskDaemon.log.error("Error occurred while updating last error date.", innerError);
        }
    }

    private T toT(ImmutableThrottlingTask task) {

        return this.taskClass.cast(task);
    }

    private static ThreadFactory getThreadFactory(String threadNameFormat) {
        return new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                var thread = new Thread(r);
                thread.setName(String.format(threadNameFormat, threadNumber.getAndIncrement()));
                thread.setDaemon(true);
                return thread;
            }
        };
    }
}

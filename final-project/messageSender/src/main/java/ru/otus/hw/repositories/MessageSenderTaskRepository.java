package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.MessageSenderTask;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface MessageSenderTaskRepository extends JpaRepository<MessageSenderTask, UUID> {

    @Query(value = """
        SELECT m FROM MessageSenderTask m
        WHERE m.cancelDate IS NULL AND m.skipping = false
        AND m.sendDate IS NULL
        AND ( m.lastErrorDate IS NULL OR m.lastErrorDate <= :time)
    """)
    List<MessageSenderTask> findTasksToStartSend(
        @Param("time") Instant actualTime
    );

}

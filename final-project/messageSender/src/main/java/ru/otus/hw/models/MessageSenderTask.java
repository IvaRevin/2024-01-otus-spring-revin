package ru.otus.hw.models;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "message_sender_task")
public class MessageSenderTask implements ImmutableThrottlingTask {

    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    private MessageState state;

    private boolean skipping;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> messageTemplateFields;

    @Enumerated(EnumType.STRING)
    private MessageTemplateType messageTemplateType;

    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    private String messageAddress;

    private Instant readyDate;

    private Instant lastErrorDate;

    private Instant sendDate;

    private Instant cancelDate;

    @CreationTimestamp
    private Instant createdDate;

    @UpdateTimestamp
    private Instant modifiedDate;

    @Version
    private long version;

    @Override
    public ImmutableThrottlingTask updateLastErrorDate() {

        return this.toBuilder()
            .lastErrorDate(Instant.now())
            .build();
    }
}

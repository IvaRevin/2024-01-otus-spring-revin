package ru.otus.hw.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.otus.hw.utils.ValidMessageType;

import java.util.Map;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageSenderTaskDTO {

    @ValidMessageType
    private String messageType;

    private Map<String, Object> messageFields;

    private String messageAddress;

    private String messageTemplateType;

}

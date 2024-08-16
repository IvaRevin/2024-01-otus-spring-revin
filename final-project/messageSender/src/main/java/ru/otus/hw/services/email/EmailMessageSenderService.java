package ru.otus.hw.services.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.config.email.EmailConfig;
import ru.otus.hw.models.MessageSenderTask;
import ru.otus.hw.models.MessageType;
import ru.otus.hw.services.MessageSenderService;
import ru.otus.hw.utils.MessageServiceType;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@MessageServiceType(MessageType.EMAIL)
@RequiredArgsConstructor
public class EmailMessageSenderService implements MessageSenderService {

    private final EmailTemplateService emailTemplateService;

    private final EmailConfig emailTemplateConfig;

    private final EmailSenderService emailSenderService;

    @Override
    public void sendMessage(MessageSenderTask task) {

        String res = this.emailTemplateService.generateEmailTemplate(
            task.getMessageTemplateFields(),
            emailTemplateConfig.getEmailTemplatesPath(),
            this.toCamelCase(task.getMessageTemplateType().name()) + ".ftl"
        );

        this.emailSenderService.sendMessage(task.getMessageAddress(), "", res);
    }

    private String toCamelCase(String enumValue) {
        return Arrays.stream(enumValue.split("_"))
            .map(word -> word.charAt(0) + word.substring(1).toLowerCase())
            .collect(Collectors.joining());
    }
}

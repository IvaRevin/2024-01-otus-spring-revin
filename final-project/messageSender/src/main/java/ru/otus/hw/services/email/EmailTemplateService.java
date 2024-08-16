package ru.otus.hw.services.email;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import freemarker.template.Configuration;
import ru.otus.hw.exceptions.EmailSenderException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailTemplateService {

    private final Configuration freMarkerConfiguration;

    public String generateEmailTemplate(Map<String, Object> model, String emailPath, String templateName) {
        try {
            Template template = freMarkerConfiguration.getTemplate(emailPath + templateName);
            StringWriter writer = new StringWriter();
            template.process(model, writer);
            return writer.toString();
        } catch (IOException | TemplateException e) {
            var errorMessage = "Failed to generate email template";
            log.error(errorMessage);
            throw new EmailSenderException(errorMessage, e);
        }
    }
}

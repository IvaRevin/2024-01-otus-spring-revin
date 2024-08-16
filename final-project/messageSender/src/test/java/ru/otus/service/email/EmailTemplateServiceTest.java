package ru.otus.service.email;

import freemarker.template.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.exceptions.EmailSenderException;
import ru.otus.hw.services.email.EmailTemplateService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EmailTemplateServiceTest {

    private Configuration freMarkerConfiguration;

    @InjectMocks
    private EmailTemplateService emailTemplateService;

    private Map<String, Object> model;

    @BeforeEach
    void setUp() throws IOException {
        freMarkerConfiguration = new Configuration(Configuration.VERSION_2_3_31);
        freMarkerConfiguration.setClassForTemplateLoading(this.getClass(), "/templates/");

        emailTemplateService = new EmailTemplateService(freMarkerConfiguration);

        model = new HashMap<>();
        model.put("name", "John Doe");
    }

    @Test
    void testGenerateEmailTemplate_success() throws Exception {
        String result = emailTemplateService.generateEmailTemplate(model, "", "TestEmailTemplate.ftl");

        String expected = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <title>Email Notification</title>
            </head>
            <body>
            <h1>Hello John Doe!</h1>

            <p>Thanks,</p>
            <p>The Team</p>
            </body>
            </html>
            """;

        assertEquals(expected.trim(), result.trim());
    }

    @Test
    void testGenerateEmailTemplate_throwsException() {
        assertThrows(EmailSenderException.class, () -> {
            emailTemplateService.generateEmailTemplate(model, "", "nonexistentTemplate.ftl");
        });
    }
}

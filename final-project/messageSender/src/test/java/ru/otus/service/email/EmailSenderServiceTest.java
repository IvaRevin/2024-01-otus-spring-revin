package ru.otus.service.email;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import ru.otus.hw.config.email.EmailConfig;
import ru.otus.hw.exceptions.EmailSenderException;
import ru.otus.hw.services.email.EmailSenderService;

@ExtendWith(MockitoExtension.class)
class EmailSenderServiceTest {

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private EmailConfig emailConfig;

    @InjectMocks
    private EmailSenderService emailSenderService;

    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() throws Exception {
        mimeMessage = new MimeMessage((Session) null);

        Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo("test@example.com");
        helper.setSubject("Test Subject");
        helper.setText("Test Text", true);
    }

    @Test
    void testSendMessage_success() {
        String fromAddress = "sender@example.com";
        String toAddress = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        Mockito.when(emailConfig.getEmailSenderAddress()).thenReturn(fromAddress);

        emailSenderService.sendMessage(toAddress, subject, text);

        verify(javaMailSender).send(mimeMessage);
    }

    @Test
    void testSendMessage_failure() {
        String fromAddress = "sender@example.com";
        String toAddress = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Text";

        Mockito.when(emailConfig.getEmailSenderAddress()).thenReturn(fromAddress);

        doThrow(new RuntimeException("Sending failed")).when(javaMailSender).send(any(MimeMessage.class));

        assertThrows(EmailSenderException.class, () -> {
            emailSenderService.sendMessage(toAddress, subject, text);
        });

        verify(javaMailSender).send(any(MimeMessage.class));
    }
}

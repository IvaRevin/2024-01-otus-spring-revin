package ru.otus.hw.services.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.email.EmailConfig;
import ru.otus.hw.exceptions.EmailSenderException;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    private final EmailConfig emailConfig;

    public void sendMessage(String to, String subject, String text) {

        try {
            var mimeMessage = javaMailSender.createMimeMessage();
            var helper = new MimeMailMessage(mimeMessage);
            mimeMessage.setContent(text, "text/html");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(emailConfig.getEmailSenderAddress());
            helper.setSubject(subject);
            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            var errorMessage = "Failed while send email template to email: " + to;
            log.error(errorMessage);
            throw new EmailSenderException(errorMessage, e);
        }
    }
}

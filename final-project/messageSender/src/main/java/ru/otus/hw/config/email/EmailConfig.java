package ru.otus.hw.config.email;

public interface EmailConfig {

    String getEmailTemplatesPath();

    String getEmailBaseTemplate();

    String getEmailSenderAddress();
}

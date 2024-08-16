package ru.otus.hw.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import ru.otus.hw.config.commons.VersionFileNameProvider;
import ru.otus.hw.config.daemons.DaemonsStartUpConfig;
import ru.otus.hw.config.daemons.MessageSenderConfig;
import ru.otus.hw.config.email.EmailConfig;

import java.time.Duration;

@Setter
@ConfigurationProperties(prefix = "application")
public class AppProperties
    implements MessageSenderConfig,
    VersionFileNameProvider,
    DaemonsStartUpConfig,
    EmailConfig {

    @Getter
    private Duration daemonInterval;

    @Getter
    private boolean daemonEnabled;

    @Getter
    private Duration daemonDelay;

    @Getter
    private String versionFileName;

    @Getter
    private int maximumPoolSize;

    @Getter
    private Duration taskThrottling;

    @Getter
    private String emailTemplatesPath;

    @Getter
    private String emailBaseTemplate;

    @Getter
    private String emailSenderAddress;

    @Override
    public Duration getMessageSenderDaemonInterval() {
        return this.daemonInterval;
    }

    @Override
    public boolean isMessageSenderDaemonEnabled() {
        return this.daemonEnabled;
    }

    @Override
    public Duration getMessageSenderDaemonDelay() {
        return this.daemonDelay;
    }

    @Override
    public int getMaximumPoolSizeForEdiDocumentGroupTaskDaemon() {
        return this.maximumPoolSize;
    }

    @Override
    public String getVersionFileName() {

        return versionFileName;
    }

    @Override
    public Duration getDaemonDelay() {

        return this.daemonDelay;
    }

    @Override
    public Duration getDaemonInterval() {

        return this.daemonInterval;
    }

    @Override
    public Duration getThrottlingTaskDuration() {
        return this.taskThrottling;
    }

    @Override
    public String getEmailTemplatesPath() {
        return this.emailTemplatesPath;
    }

    @Override
    public String getEmailBaseTemplate() {
        return this.emailBaseTemplate;
    }

    @Override
    public String getEmailSenderAddress() {
        return this.emailSenderAddress;
    }
}

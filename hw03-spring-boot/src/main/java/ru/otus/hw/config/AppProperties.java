package ru.otus.hw.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@ConfigurationProperties(prefix = "application")
public class AppProperties implements TestConfig, TestFileNameProvider, LocaleConfig {

    @Getter
    @Setter
    private int rightAnswersCountToPass;

    @Getter
    private Locale locale;

    @Setter
    private Map<String, String> fileNameByLocaleTag;

    public void setLocale(String locale) {
        this.locale = Locale.forLanguageTag(locale);
    }

    @Override
    public String getTestFileName() {
        return fileNameByLocaleTag.get(locale.toLanguageTag());
    }
}

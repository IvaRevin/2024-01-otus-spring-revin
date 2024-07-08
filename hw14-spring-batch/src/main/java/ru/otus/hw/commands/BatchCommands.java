package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import static ru.otus.hw.config.JobConfig.CURRENT_TIME_PARAM_NAME;

@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class BatchCommands {

    private final Job importFromDatabaseJob;

    private final JobLauncher jobLauncher;

    @ShellMethod(value = "processMigration", key = "pm")
    public void processMigrationJob() throws Exception {
        JobExecution execution = jobLauncher.run(importFromDatabaseJob, new JobParametersBuilder()
            .addLong(CURRENT_TIME_PARAM_NAME, System.currentTimeMillis())
            .toJobParameters());
        log.info(String.valueOf(execution));
    }
}

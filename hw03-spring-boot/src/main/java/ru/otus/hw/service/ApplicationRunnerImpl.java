package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Profile("!test")
@Service
public class ApplicationRunnerImpl implements CommandLineRunner {

    private final TestRunnerServiceImpl testRunnerServiceImpl;

    @Override
    public void run(String... args) throws Exception {

        testRunnerServiceImpl.run();
    }
}

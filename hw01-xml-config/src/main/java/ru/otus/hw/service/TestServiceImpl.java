package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        questionDao.findAll().forEach(questionDao -> ioService.printLine(questionAnswersFormatter(questionDao)));
    }

    private String questionAnswersFormatter(Question question) {

        if (question == null || question.text().isBlank()) {
            return null;
        }

        String header = String.format("%s \n", question.text());
        if (question.answers() == null || question.answers().isEmpty()) {
            return header;
        }

        return IntStream.range(0, question.answers().size())
            .mapToObj(index -> {
                Answer answer = question.answers().get(index);
                if (answer.text() != null && !answer.text().isBlank()) {
                    return String.format("%d) %s", index + 1, answer.text().trim());
                } else {
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.joining("\n", header, "\n"));
    }
}

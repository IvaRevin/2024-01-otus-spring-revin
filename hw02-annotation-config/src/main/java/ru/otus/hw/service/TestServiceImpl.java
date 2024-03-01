package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {

            var answerIndex = ioService.readIntForRangeWithPrompt(
                1,
                question.answers().size(),
                questionAnswersFormatter(question),
                "Accept only numbers from 1 to " + question.answers().size()
            );

            if (answerIndex == 0) {
                answerIndex = 1;
            }
            Answer answer = question.answers().get(answerIndex - 1);

            var isAnswerValid = answer.isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
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

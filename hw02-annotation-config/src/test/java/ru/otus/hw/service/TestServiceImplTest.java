package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DisplayName("Тест TestServiceImpl")
@ExtendWith(MockitoExtension.class)
public class TestServiceImplTest {

    private static IOService ioService;
    private static QuestionDao questionDao;
    private static TestServiceImpl testService;

    @BeforeAll
    static void setUp() {
        ioService = Mockito.mock(IOService.class);
        questionDao = Mockito.mock(QuestionDao.class);
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    @DisplayName("Корректно выполняет тестирование студента")
    void executeTestForStudentCorrectly() {

        Student student = new Student("Ivan", "Ivanov");
        List<Question> questionList = List.of(
            new Question("Question 1", List.of(new Answer("1", true))),
            new Question("Question 2", List.of(new Answer("2", false)))
        );

        when(questionDao.findAll()).thenReturn(questionList);
        when(ioService.readIntForRangeWithPrompt(anyInt(), anyInt(), anyString(), anyString()))
            .thenReturn(1)
            .thenReturn(1);

        TestResult testResult = testService.executeTestFor(student);

       assertEquals(1, testResult.getRightAnswersCount());
    }

    @Test
    @DisplayName("Корректно обрабатывает пустой список вопросов")
    void handlesEmptyQuestionListCorrectly() {
        Student student = new Student("Petr", "Petrov");

        when(questionDao.findAll()).thenReturn(Collections.emptyList());

        TestResult testResult = testService.executeTestFor(student);

        assertEquals(0, testResult.getAnsweredQuestions().size());
    }
}

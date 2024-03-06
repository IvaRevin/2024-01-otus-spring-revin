package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.exceptions.QuestionReadException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@DisplayName("Тест CsvQuestionDao")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class CsvQuestionDaoTest {

    @Mock
    private AppProperties appProperties;

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Order(1)
    @DisplayName("Ожидается ошибка на отсутствие файла")
    @Test
    void shouldThrowFileNotFoundException() {
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

    @Order(2)
    @DisplayName("Ожидается ошибка неверного формата вопроса")
    @Test
    void shouldThrowUnsupportedQuestionFormatException() {
        when(appProperties.getTestFileName()).thenReturn("fail-test-questions.csv");
        assertThrows(QuestionReadException.class, csvQuestionDao::findAll);
    }

    @Order(3)
    @DisplayName("Проверка что нет ошибок при парсинге.")
    @Test
    void shouldReturnEmptyListWhenFileIsEmpty() {
        when(appProperties.getTestFileName()).thenReturn("valid-test-questions.csv");
        assertDoesNotThrow(csvQuestionDao::findAll);
    }

}

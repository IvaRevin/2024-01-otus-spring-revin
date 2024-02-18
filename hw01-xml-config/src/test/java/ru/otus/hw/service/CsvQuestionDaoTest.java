package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

@DisplayName("Тест CsvQuestionDao")
public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider fileNameProvider;

    private CsvQuestionDao csvQuestionDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(fileNameProvider.getTestFileName()).thenReturn("questions.csv");
        csvQuestionDao = new CsvQuestionDao(fileNameProvider);
    }

    @DisplayName("Проверяем что нет ошибок при парсинге.")
    @Test
    void findAll_ShouldReturnEmptyListWhenFileIsEmpty() {
        assertDoesNotThrow(csvQuestionDao::findAll);
    }

}

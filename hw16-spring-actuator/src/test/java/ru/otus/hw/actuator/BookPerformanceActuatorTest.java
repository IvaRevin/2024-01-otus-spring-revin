package ru.otus.hw.actuator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.health.checkers.BookPerformanceActuator;
import ru.otus.hw.repositories.BookRepository;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BookPerformanceActuator.class,
    properties = { "spring.sql.init.mode=never" })
@EnableAutoConfiguration
@AutoConfigureMockMvc
@DisplayName("Тестирование актуатора производительности книг")
class BookPerformanceActuatorTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookRepository bookRepository;

    @Test
    @DisplayName("Проверка времени ответа репозитория книг")
    void getBookPerformance() throws Exception {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/actuator/health"))
            .andExpect(status().isOk());
    }
}

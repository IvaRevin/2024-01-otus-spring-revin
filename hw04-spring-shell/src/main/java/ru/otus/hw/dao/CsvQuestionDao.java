package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())
        ) {
            if (inputStream == null) {
                throw new FileNotFoundException("File with test questions not found.");
            }

            var questionDtoList = new CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                .withType(QuestionDto.class)
                .withSeparator(';')
                .withSkipLines(1)
                .withExceptionHandler(e -> {
                    throw new QuestionReadException(e.getMessage(), e);
                })
                .build()
                .parse();

            return questionDtoList.stream()
                .map(QuestionDto::toDomainObject)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new QuestionReadException("Error when reading list of questions", e);
        }
    }
}

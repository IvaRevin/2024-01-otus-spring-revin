package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    @NonNull
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        try (
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())
        ) {
            if (inputStream == null) {
                return Collections.emptyList();
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

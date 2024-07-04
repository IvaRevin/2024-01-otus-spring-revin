package ru.otus.hw.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ru.otus.hw.models.mongo.MongoAuthor;
import ru.otus.hw.models.mongo.MongoBook;
import ru.otus.hw.models.mongo.MongoComment;
import ru.otus.hw.models.mongo.MongoGenre;
import ru.otus.hw.repositories.mongo.MongoAuthorRepository;
import ru.otus.hw.repositories.mongo.MongoBookRepository;
import ru.otus.hw.repositories.mongo.MongoCommentRepository;
import ru.otus.hw.repositories.mongo.MongoGenreRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureDataMongo
@SpringBatchTest
@Import(JobConfig.class)
public class JobConfigTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private MongoAuthorRepository authorRepository;

    @Autowired
    private MongoGenreRepository genreRepository;

    @Autowired
    private MongoBookRepository bookRepository;

    @Autowired
    private MongoCommentRepository commentRepository;

    @Test
    @DisplayName("Тест выгрузки записей в MongoDB")
    void importFromDatabaseJob() throws Exception {
        Job job = jobLauncherTestUtils.getJob();
        assertThat(job).isNotNull()
            .extracting(Job::getName)
            .isEqualTo(JobConfig.IMPORT_FROM_DATABASE_JOB_NAME);

        JobParameters parameters = new JobParametersBuilder()
            .addLong(JobConfig.CURRENT_TIME_PARAM_NAME, System.currentTimeMillis())
            .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

        List<MongoBook> actualBooks = bookRepository.findAll().stream()
            .map(this::initializeBookLazyFields)
            .collect(Collectors.toList());

        List<MongoAuthor> actualAuthors = authorRepository.findAll().stream()
            .map(this::initializeAuthorLazyFields)
            .collect(Collectors.toList());

        List<MongoGenre> actualGenres = genreRepository.findAll().stream()
            .map(this::initializeGenreLazyFields)
            .collect(Collectors.toList());

        List<MongoComment> actualComments = commentRepository.findAll().stream()
            .map(this::initializeCommentLazyFields)
            .collect(Collectors.toList());

        assertThat(actualBooks)
            .isNotEmpty()
            .hasSameElementsAs(getExampleBookList());

        assertThat(actualComments)
            .isNotEmpty()
            .hasSameElementsAs(getExampleCommentList());

        assertThat(actualAuthors)
            .isNotEmpty()
            .hasSameElementsAs(getExampleAuthorList());

        assertThat(actualGenres)
            .isNotEmpty()
            .hasSameElementsAs(getExampleGenreList());
    }

    private MongoBook initializeBookLazyFields(MongoBook book) {
        // Принудительная инициализация ленивых коллекций
        return new MongoBook(book.getId(), book.getTitle(), book.getAuthor(), book.getGenres().stream().collect(Collectors.toList()));
    }

    private MongoAuthor initializeAuthorLazyFields(MongoAuthor author) {
        return new MongoAuthor(author.getId(), author.getFullName());
    }

    private MongoGenre initializeGenreLazyFields(MongoGenre genre) {
        return new MongoGenre(genre.getId(), genre.getName());
    }

    private MongoComment initializeCommentLazyFields(MongoComment comment) {
        return new MongoComment(comment.getId(), comment.getText(), initializeBookLazyFields(comment.getBook()));
    }

    private List<MongoAuthor> getExampleAuthorList() {
        return List.of(new MongoAuthor("1", "Author_1"),
            new MongoAuthor("2", "Author_2"),
            new MongoAuthor("3", "Author_3"));
    }

    private List<MongoGenre> getExampleGenreList() {
        return List.of(new MongoGenre("1", "Genre_1"),
            new MongoGenre("2", "Genre_2"),
            new MongoGenre("3", "Genre_3"),
            new MongoGenre("4", "Genre_4"),
            new MongoGenre("5", "Genre_5"),
            new MongoGenre("6", "Genre_6"));
    }

    private List<MongoBook> getExampleBookList() {
        List<MongoGenre> genreList = getExampleGenreList();
        List<MongoAuthor> authorList = getExampleAuthorList();

        return List.of(new MongoBook("1", "BookTitle_1", authorList.get(0), List.of(genreList.get(0), genreList.get(1))),
            new MongoBook("2", "BookTitle_2", authorList.get(1), List.of(genreList.get(2), genreList.get(3))),
            new MongoBook("3", "BookTitle_3", authorList.get(2), List.of(genreList.get(4), genreList.get(5))));
    }

    private List<MongoComment> getExampleCommentList() {
        List<MongoBook> bookList = getExampleBookList();

        return List.of(new MongoComment("1", "Book_1_Comment_1", bookList.get(0)),
            new MongoComment("2", "Book_1_Comment_2", bookList.get(0)),
            new MongoComment("3", "Book_1_Comment_3", bookList.get(0)),
            new MongoComment("4", "Book_2_Comment_1", bookList.get(1)),
            new MongoComment("5", "Book_3_Comment_1", bookList.get(2)));
    }
}

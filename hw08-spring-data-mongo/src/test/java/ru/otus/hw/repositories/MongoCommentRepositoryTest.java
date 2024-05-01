package ru.otus.hw.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе MongoDB для работы с комментариями к книгам")
@DataMongoTest
public class MongoCommentRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    @DisplayName("Очищает тестовую бд перед тестом.")
    public void dropTable() {

        mongoTemplate.getDb().drop();
    }

    @Test
    @DisplayName("Проверят что метод findById с существующим ID должен возвращать комментарий")
    void findByIdExistingCommentShouldReturnComment() {
        Book book = new Book("1", "Book 1", null, null);
        mongoTemplate.save(book);
        Comment comment = new Comment("1", "Book_1_Comment", book);
        mongoTemplate.save(comment);

        Optional<Comment> foundComment = commentRepository.findById("1");
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getText()).isEqualTo("Book_1_Comment");
    }

    @Test
    @DisplayName("Проверят что метод findAllByBookId для существующей книги должен возвращать список комментариев")
    void findAllByBookIdExistingBookShouldReturnComments() {
        Book book = new Book("1", "Book 1", null, null);
        mongoTemplate.save(book);
        Comment comment1 = new Comment(null, "Comment 1 for Book_1", book);
        Comment comment2 = new Comment(null, "Comment 2 for Book_1", book);
        mongoTemplate.save(comment1);
        mongoTemplate.save(comment2);

        List<Comment> comments = commentRepository.findAllByBookId("1");
        assertThat(comments).isNotEmpty();
        assertThat(comments.get(0).getBook().getId()).isEqualTo("1");
    }

    @Test
    @DisplayName("Проверяет что метод save для нового комментария должен сохранять его в базе данных")
    void saveNewCommentShouldPersist() {
        Book book = new Book("1", "Book 1", null, null);
        mongoTemplate.save(book);

        Comment newComment = new Comment(null, "New comment for Book_1", book);
        Comment savedComment = commentRepository.save(newComment);

        assertThat(savedComment.getText()).isEqualTo("New comment for Book_1");
        assertThat(savedComment.getBook().getId()).isEqualTo(book.getId());
    }

    @Test
    @DisplayName("Проверяет что метод deleteById с существующим ID комментария должен удалять комментарий")
    void deleteByIdExistingCommentShouldRemoveComment() {
        Book book = new Book("1", "Book to be deleted", null, null);
        mongoTemplate.save(book);
        Comment comment = new Comment(null, "Comment to be deleted", book);
        comment = mongoTemplate.save(comment);
        String commentId = comment.getId();

        commentRepository.deleteById(commentId);

        Optional<Comment> deletedComment = commentRepository.findById(commentId);
        assertThat(deletedComment).isNotPresent();
    }
}

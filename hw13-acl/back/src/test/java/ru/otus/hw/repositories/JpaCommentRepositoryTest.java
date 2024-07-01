package ru.otus.hw.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Репозиторий на основе JPA для работы с комментариями к книгам")
@DataJpaTest
public class JpaCommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Проверят что метод findById с существующим ID должен возвращать комментарий")
    void findByIdExistingCommentShouldReturnComment() {
        Optional<Comment> foundComment = commentRepository.findById(1L);
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getText()).contains("Book_1_Comment");
    }

    @Test
    @DisplayName("Проверят что метод findAllByBookId для существующей книги должен возвращать список комментариев")
    void findAllByBookIdExistingBookShouldReturnComments() {
        List<Comment> comments = commentRepository.findAllByBookId(1L);
        assertThat(comments).isNotEmpty();
        assertThat(comments.get(0).getBook().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Проверяет что метод save для нового комментария должен сохранять его в базе данных")
    void saveNewCommentShouldPersist() {
        Book book = entityManager.find(Book.class, 1L);

        Comment newComment = new Comment();
        newComment.setText("New comment for Book_1");
        newComment.setBook(book);

        Comment savedComment = commentRepository.save(newComment);
        entityManager.flush();

        assertThat(savedComment.getText()).isEqualTo("New comment for Book_1");
        assertThat(savedComment.getBook().getId()).isEqualTo(book.getId());
    }

    @Test
    @DisplayName("Проверяет что метод deleteById с существующим ID комментария должен удалять комментарий")
    void deleteByIdExistingCommentShouldRemoveComment() {
        Comment comment = entityManager.persistFlushFind(new Comment(0, "Comment to be deleted", entityManager.find(Book.class, 1L)));
        Long commentId = comment.getId();

        commentRepository.deleteById(commentId);
        entityManager.flush();

        Comment deletedComment = entityManager.find(Comment.class, commentId);
        assertThat(deletedComment).isNull();
    }
}

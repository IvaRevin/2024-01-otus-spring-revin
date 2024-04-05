package ru.otus.hw.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Comment;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllByBookId(Long bookId) {
       return entityManager.createQuery("SELECT c FROM Comment c WHERE c.book.id = :book_id", Comment.class)
           .setParameter("book_id", bookId)
           .getResultList();
    }

    @Override
    public Comment save(Comment comment) {
        if (comment.getId() == 0) {
            entityManager.persist(comment);
            return comment;
        }
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(Long id) {
        Comment comment = entityManager.find(Comment.class, id);
        if (comment != null) {
            entityManager.remove(comment);
        }
    }
}

package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final BookRepository bookRepository;

    private final CommentRepository commentRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findById(String id) {

        return commentRepository.findById(id)
            .map(CommentDTO::commentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findAllByBookId(String bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
            .map(CommentDTO::commentToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO insert(String text, String bookId) {
        return save("0", text, bookId);
    }

    @Override
    @Transactional
    public CommentDTO update(String id, String text, String bookId) {
        return save(id, text, bookId);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO save(String id, String text, String bookId) {

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Book with id %s not found".formatted(bookId)));

        Comment comment;

        if (id.equals("0")) {
            comment = new Comment("0", text, book);
        } else {

            comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Comment with id = %s not found".formatted(id)));
            comment.setText(text);
        }

        return CommentDTO.commentToDto(commentRepository.save(comment));
    }
}

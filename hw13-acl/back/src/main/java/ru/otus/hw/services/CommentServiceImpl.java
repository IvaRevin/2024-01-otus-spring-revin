package ru.otus.hw.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.dtos.CommentCreateDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.dtos.CommentEditDTO;
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
    public Optional<CommentDTO> findById(long id) {

        return commentRepository.findById(id)
            .map(CommentDTO::commentToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> findAllByBookId(long bookId) {
        return commentRepository.findAllByBookId(bookId).stream()
            .map(CommentDTO::commentToDto)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO insert(CommentCreateDTO dto) {
        String text = dto.getText();
        Long bookId = dto.getBookId();
        return save(0, text, bookId);
    }

    @Override
    @Transactional
    public CommentDTO update(CommentEditDTO dto) {
        Long id = dto.getId();
        String text = dto.getText();
        Long bookId = dto.getBookId();
        return save(id, text, bookId);
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }

    private CommentDTO save(long id, String text, long bookId) {

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException(
                "Book with id %d not found".formatted(bookId)));

        Comment comment;

        if (id == 0) {
            comment = new Comment(0L, text, book);
        } else {

            comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Comment with id = %d not found".formatted(id)));
            comment.setText(text);
        }

        return CommentDTO.commentToDto(commentRepository.save(comment));
    }
}

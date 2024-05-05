package ru.otus.hw.controlles;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.otus.hw.dtos.BookDTO;
import ru.otus.hw.dtos.CommentDTO;
import ru.otus.hw.services.BookServiceImpl;
import ru.otus.hw.services.CommentServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    private final BookServiceImpl bookService;

    @GetMapping("/book_comments")
    public String getCommentBookList(@RequestParam("id") long id, Model model) {
        BookDTO book = bookService.findById(id).orElse(null);
        List<CommentDTO> commentDTOList = commentService.findAllByBookId(id);

        model.addAttribute("book", book);
        model.addAttribute("comments", commentDTOList);

        return "comments/comments";
    }
}

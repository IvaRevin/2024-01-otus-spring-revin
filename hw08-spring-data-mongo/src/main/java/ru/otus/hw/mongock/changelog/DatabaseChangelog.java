package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;
import ru.otus.hw.models.Comment;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Arrays;

@ChangeLog
public class DatabaseChangelog {

    @ChangeSet(order = "001", id = "dropTables", author = "ivan", runAlways = true)
    public void dropTables(MongoDatabase database) {
        database.drop();
    }

    @ChangeSet(order = "002", id = "initAuthors", author = "ivan")
    public void initAuthors(AuthorRepository authorRepository) {
        List<Author> authors = Arrays.asList(
            new Author("1", "Author_1"),
            new Author("2", "Author_2"),
            new Author("3", "Author_3")
        );
        authorRepository.saveAll(authors);
    }

    @ChangeSet(order = "003", id = "initGenres", author = "ivan")
    public void initGenres(GenreRepository genreRepository) {
        List<Genre> genres = Arrays.asList(
            new Genre("1", "Genre_1"),
            new Genre("2", "Genre_2"),
            new Genre("3", "Genre_3"),
            new Genre("4", "Genre_4"),
            new Genre("5", "Genre_5"),
            new Genre("6", "Genre_6")
        );
        genreRepository.saveAll(genres);
    }

    @ChangeSet(order = "004", id = "initBooks", author = "ivan")
    public void initBooks(
        AuthorRepository authorRepository,
        GenreRepository genreRepository,
        BookRepository bookRepository
    ) {

        List<Author> authors = authorRepository.findAll();
        List<Genre> genres = genreRepository.findAll();

        Book book1 = new Book("1", "BookTitle_1", authors.get(0), genres.subList(0, 2));
        Book book2 = new Book("2", "BookTitle_2", authors.get(1), genres.subList(2, 4));
        Book book3 = new Book("3", "BookTitle_3", authors.get(2), genres.subList(4, 6));

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
    }

    @ChangeSet(order = "005", id = "initComments", author = "ivan")
    public void initComments(BookRepository bookRepository, CommentRepository commentRepository) {
        List<Book> books = bookRepository.findAll();

        List<Comment> comments = Arrays.asList(
            new Comment("1", "Не читал", books.get(0)),
            new Comment("2", "Читал", books.get(0)),
            new Comment("3", "Понравилось", books.get(0)),
            new Comment("4", "Не понравилось", books.get(1)),
            new Comment("5", "Сюжет не впечатлил", books.get(2))
        );
        commentRepository.saveAll(comments);
    }
}

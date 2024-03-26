package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotUpdateException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {

    private final GenreRepository genreRepository;

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public Optional<Book> findById(long id) {
        return Optional.ofNullable(jdbcOperations.query(
            "SELECT b.id, b.title, a.id as author_id, a.full_name, g.id as genre_id, g.name " +
                "FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.id " +
                "LEFT JOIN books_genres bg ON b.id = bg.book_id " +
                "LEFT JOIN genres g ON bg.genre_id = g.id " +
                "WHERE b.id = :id",
            Map.of("id", id),
            new BookResultSetExtractor()));
    }

    @Override
    public List<Book> findAll() {
        var genres = genreRepository.findAll();
        var relations = getAllGenreRelations();
        var books = getAllBooksWithoutGenres();
        mergeBooksInfo(books, genres, relations);
        return books;
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        jdbcOperations.update(
            "DELETE FROM books WHERE id=:id",
            Map.of("id", id)
        );
    }

    private List<Book> getAllBooksWithoutGenres() {
        return jdbcOperations.query(
            "SELECT b.id, b.title, b.author_id, a.full_name AS author_name FROM books b " +
                "INNER JOIN authors a ON b.author_id = a.id",
            new BookRowMapper());
    }

    private List<BookGenreRelation> getAllGenreRelations() {
        return jdbcOperations.query(
            "SELECT book_id, genre_id FROM books_genres",
            (rs, rowNum) -> new BookGenreRelation(rs.getLong("book_id"), rs.getLong("genre_id"))
        );
    }

    private void mergeBooksInfo(List<Book> booksWithoutGenres, List<Genre> genres,
                                List<BookGenreRelation> relations) {
        Map<Long, Genre> genreMap = genres.stream().collect(Collectors.toMap(Genre::getId, Function.identity()));
        for (Book book : booksWithoutGenres) {
            List<Genre> bookGenres = relations.stream()
                .filter(relation -> relation.bookId() == book.getId())
                .map(relation -> genreMap.get(relation.genreId()))
                .collect(Collectors.toList());
            book.setGenres(bookGenres);
        }
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("author_id", book.getAuthor().getId());

        jdbcOperations.update(
                "INSERT INTO books (title, author_id) VALUES (:title, :author_id)",
            parameterSource,
            keyHolder,
            new String[]{"id"}
        );

        //noinspection DataFlowIssue
        book.setId(keyHolder.getKeyAs(Long.class));
        batchInsertGenresRelationsFor(book);
        return book;
    }

    private Book update(Book book) {

        int res = jdbcOperations.update(
            "UPDATE books SET title = :title, author_id = :author_id WHERE id = :id ",
            Map.of(
                "id", book.getId(),
                "title", book.getTitle(),
                "author_id", book.getAuthor().getId()
            ));

        if (res <= 0) {
            throw new EntityNotUpdateException(book);
        }

        removeGenresRelationsFor(book);
        batchInsertGenresRelationsFor(book);

        return book;
    }

    private void batchInsertGenresRelationsFor(Book book) {
        if (book.getGenres() == null || book.getGenres().isEmpty()) {
            return;
        }

        List<MapSqlParameterSource> parameters = book.getGenres().stream()
            .map(genre -> new MapSqlParameterSource()
                .addValue("book_id", book.getId())
                .addValue("genre_id", genre.getId()))
            .toList();

        jdbcOperations.batchUpdate(
            "INSERT INTO books_genres (book_id, genre_id) VALUES (:book_id, :genre_id)",
            parameters.toArray(new MapSqlParameterSource[0])
        );
    }

    private void removeGenresRelationsFor(Book book) {
        jdbcOperations.update(
            "DELETE FROM books_genres WHERE book_id = :bookId",
            Map.of("bookId", book.getId())
        );
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
            return new Book(rs.getLong("id"), rs.getString("title"), author, null);
        }
    }

    // Использовать для findById
    @SuppressWarnings("ClassCanBeRecord")
    @RequiredArgsConstructor
    private static class BookResultSetExtractor implements ResultSetExtractor<Book> {

        @Override
        public Book extractData(ResultSet rs) throws SQLException, DataAccessException {
            Book book = null;
            List<Genre> genres = new ArrayList<>();
            while (rs.next()) {
                if (book == null) {
                    Author author = new Author(rs.getLong("author_id"), rs.getString("full_name"));
                    book = new Book(rs.getLong("id"), rs.getString("title"), author, genres);
                }
                long genreId = rs.getLong("genre_id");
                if (genreId > 0) {
                    Genre genre = new Genre(genreId, rs.getString("name"));
                    genres.add(genre);
                }
            }
            return book;
        }
    }

    private record BookGenreRelation(long bookId, long genreId) {
    }
}

package ru.otus.hw.repositories;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Author> findAll() {

        return jdbcOperations.getJdbcOperations().query(
            "SELECT id, full_name FROM authors",
            new AuthorRowMapper()
        );
    }

    @Override
    public Optional<Author> findById(long id) {

        List<Author> authorList = jdbcOperations.query(
            "SELECT id, full_name FROM authors WHERE id=:id",
            Map.of("id", id),
            new AuthorRowMapper()
        );

        if (authorList.size() == 1) {
            return Optional.of(authorList.get(0));
        }
        return Optional.empty();
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(rs.getLong("id"), rs.getString("full_name"));
        }
    }
}

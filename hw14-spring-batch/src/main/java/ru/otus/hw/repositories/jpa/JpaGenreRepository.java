package ru.otus.hw.repositories.jpa;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.otus.hw.models.jpa.JpaGenre;

import java.util.List;
import java.util.Set;

public interface JpaGenreRepository extends PagingAndSortingRepository<JpaGenre, Long> {

    List<JpaGenre> findAllByIdIn(Set<Long> ids);
}

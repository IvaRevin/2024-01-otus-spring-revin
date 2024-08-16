package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

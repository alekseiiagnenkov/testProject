package ru.itfb.testproject.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itfb.testproject.model.Person;

/**
 * Репозиторий для {@link Person}
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}

package pt.jorgeduarte.concurrency_test.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.jorgeduarte.concurrency_test.domain.entities.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
}

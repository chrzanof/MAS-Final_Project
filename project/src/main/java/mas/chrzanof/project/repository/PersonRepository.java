package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
} 
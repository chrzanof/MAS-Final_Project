package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Quiz;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
} 
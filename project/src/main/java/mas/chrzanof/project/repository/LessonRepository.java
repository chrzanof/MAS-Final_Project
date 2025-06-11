package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
} 
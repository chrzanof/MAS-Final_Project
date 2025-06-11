package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
} 
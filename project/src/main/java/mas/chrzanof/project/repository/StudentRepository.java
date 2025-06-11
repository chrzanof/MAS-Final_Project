package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentId(Long studentId);
    Student findByPersonId(Long personId);
} 
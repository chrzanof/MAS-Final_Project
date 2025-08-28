package mas.chrzanof.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Teacher;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
    @Query("SELECT DISTINCT c FROM Course c " +
           "LEFT JOIN FETCH c.lessons " +
           "LEFT JOIN FETCH c.quizzes " +
           "WHERE c.teacherInCharge = :teacher " +
           "ORDER BY c.id")
    List<Course> findCoursesByTeacherInChargeWithLessonsAndQuizzes(@Param("teacher") Teacher teacher);
} 
package mas.chrzanof.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.TeacherRepository;

@Service
@Transactional
public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public boolean isAssignedToCourse(Teacher teacher, Course course) {
        return teacher.getAssignedCourses().contains(course);
    }

    public boolean isInChargeOfCourse(Teacher teacher, Course course) {
        return teacher.getCoursesInCharge().contains(course);
    }

    public Teacher removeFromCourse(Teacher teacher, Course course) {
        if (course == null) {
            throw new IllegalArgumentException("Course cannot be null");
        }
        teacher.getAssignedCourses().remove(course);
        teacher.getCoursesInCharge().remove(course);
        return teacherRepository.save(teacher);
    }
} 
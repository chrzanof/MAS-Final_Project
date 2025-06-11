package mas.chrzanof.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.CourseRepository;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public Course assignTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        course.getAssignedTeachers().add(teacher);
        teacher.getAssignedCourses().add(course);
        return courseRepository.save(course);
    }

    public Course removeTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        course.getAssignedTeachers().remove(teacher);
        teacher.getAssignedCourses().remove(course);
        if (course.getTeacherInCharge() != null && course.getTeacherInCharge().equals(teacher)) {
            course.setTeacherInCharge(null);
        }
        return courseRepository.save(course);
    }

    public Course setTeacherInCharge(Course course, Teacher teacher) {
        if (teacher != null && !course.getAssignedTeachers().contains(teacher)) {
            throw new IllegalStateException("Teacher must be assigned to the course before being in charge of it");
        }
        if (course.getTeacherInCharge() != null) {
            course.getTeacherInCharge().getCoursesInCharge().remove(course);
        }
        course.setTeacherInCharge(teacher);
        if (teacher != null) {
            teacher.getCoursesInCharge().add(course);
        }
        return courseRepository.save(course);
    }

    public boolean isTeacherAssigned(Course course, Teacher teacher) {
        return course.getAssignedTeachers().contains(teacher);
    }

    public boolean isTeacherInCharge(Course course, Teacher teacher) {
        return course.getTeacherInCharge() != null && course.getTeacherInCharge().equals(teacher);
    }
} 
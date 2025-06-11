package mas.chrzanof.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.CourseRepository;
import mas.chrzanof.project.repository.TeacherRepository;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService(CourseRepository courseRepository, TeacherRepository teacherRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
    }

    @Transactional
    public Course assignTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        
        // Refresh the course from the database to ensure we have the latest state
        Course refreshedCourse = courseRepository.findById(course.getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        // Check if the teacher is already assigned to the course
        if (!refreshedCourse.getAssignedTeachers().contains(teacher)) {
            refreshedCourse.getAssignedTeachers().add(teacher);
            teacher.getAssignedCourses().add(refreshedCourse);
            // Save only the course - the teacher relationship will be saved through cascade
            return courseRepository.save(refreshedCourse);
        }
        return refreshedCourse;
    }

    @Transactional
    public Course removeTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        
        // Refresh the course from the database to ensure we have the latest state
        Course refreshedCourse = courseRepository.findById(course.getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        if (refreshedCourse.getAssignedTeachers().contains(teacher)) {
            refreshedCourse.getAssignedTeachers().remove(teacher);
            teacher.getAssignedCourses().remove(refreshedCourse);
            if (refreshedCourse.getTeacherInCharge() != null && refreshedCourse.getTeacherInCharge().equals(teacher)) {
                refreshedCourse.setTeacherInCharge(null);
            }
            // Save only the course - the teacher relationship will be saved through cascade
            return courseRepository.save(refreshedCourse);
        }
        return refreshedCourse;
    }

    @Transactional
    public Course setTeacherInCharge(Course course, Teacher teacher) {
        // Refresh the course from the database to ensure we have the latest state
        Course refreshedCourse = courseRepository.findById(course.getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
            
        if (teacher != null && !refreshedCourse.getAssignedTeachers().contains(teacher)) {
            throw new IllegalStateException("Teacher must be assigned to the course before being in charge of it");
        }
        
        Teacher previousTeacher = refreshedCourse.getTeacherInCharge();
        if (previousTeacher != null) {
            previousTeacher.getCoursesInCharge().remove(refreshedCourse);
        }
        
        refreshedCourse.setTeacherInCharge(teacher);
        if (teacher != null) {
            teacher.getCoursesInCharge().add(refreshedCourse);
        }
        
        // Save only the course - the teacher relationships will be saved through cascade
        return courseRepository.save(refreshedCourse);
    }

    public boolean isTeacherAssigned(Course course, Teacher teacher) {
        return course.getAssignedTeachers().contains(teacher);
    }

    public boolean isTeacherInCharge(Course course, Teacher teacher) {
        return course.getTeacherInCharge() != null && course.getTeacherInCharge().equals(teacher);
    }
} 
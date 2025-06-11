package mas.chrzanof.project.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Enrollment;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.repository.CourseRepository;
import mas.chrzanof.project.repository.EnrollmentRepository;
import mas.chrzanof.project.repository.StudentRepository;

@Service
@Transactional
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                           CourseRepository courseRepository,
                           StudentRepository studentRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    public Enrollment enrollStudent(Course course, Student student) {
        // Check if student is already enrolled
        if (enrollmentRepository.existsByStudentIdAndCourseId(student.getId(), course.getId())) {
            throw new IllegalStateException("Student is already enrolled in this course");
        }

        // Create new enrollment
        Enrollment enrollment = new Enrollment(course, student);
        
        // Add enrollment to both sides of the relationship
        course.addEnrollment(enrollment);
        student.addEnrollment(enrollment);
        
        // Save the course which will cascade to the enrollment
        return courseRepository.save(course).getEnrollments().stream()
            .filter(e -> e.getStudent().equals(student))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Failed to create enrollment"));
    }

    @Transactional
    public void unenrollStudent(Course course, Student student) {
        course.getEnrollments().stream()
            .filter(e -> e.getStudent().equals(student))
            .findFirst()
            .ifPresent(enrollment -> {
                course.removeEnrollment(enrollment);
                student.removeEnrollment(enrollment);
                courseRepository.save(course);
            });
    }
} 
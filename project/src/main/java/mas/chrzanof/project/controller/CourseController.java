package mas.chrzanof.project.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import mas.chrzanof.project.dto.CourseDTO;
import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Lesson;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Quiz;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.service.CourseService;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final PersonRepository personRepository;

    @Autowired
    public CourseController(CourseService courseService, PersonRepository personRepository) {
        this.courseService = courseService;
        this.personRepository = personRepository;
    }

    @PostMapping
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.createCourse(course));
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<Course> addLessonToCourse(
            @PathVariable Long courseId,
            @RequestBody Lesson lesson) {
        return ResponseEntity.ok(courseService.addLessonToCourse(courseId, lesson));
    }

    @PostMapping("/{courseId}/quizzes")
    public ResponseEntity<Course> addQuizToCourse(
            @PathVariable Long courseId,
            @RequestBody Quiz quiz) {
        return ResponseEntity.ok(courseService.addQuizToCourse(courseId, quiz));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseDTO>> getMyCourses(HttpServletRequest request) {
        // Get current user from session
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return ResponseEntity.status(401).build();
        }

        Long userId = (Long) session.getAttribute("userId");
        Optional<Person> personOpt = personRepository.findById(userId);
        
        if (personOpt.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        Person person = personOpt.get();
        Teacher teacher = person.getTeacher();
        
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }

        List<CourseDTO> courses = courseService.getCoursesByTeacherInCharge(teacher);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(
            @PathVariable Long id,
            @RequestBody Course course) {
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
} 
package mas.chrzanof.project.controller;

import mas.chrzanof.project.dto.CourseDTO;
import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Lesson;
import mas.chrzanof.project.model.Quiz;
import mas.chrzanof.project.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
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
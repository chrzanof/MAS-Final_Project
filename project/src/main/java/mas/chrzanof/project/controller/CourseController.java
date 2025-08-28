package mas.chrzanof.project.controller;

import java.util.List;

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

    private Teacher getAuthenticatedTeacher(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            return null; 
        }

        Long userId = (Long) session.getAttribute("userId");
        Person person = personRepository.findById(userId).orElse(null);
        
        if (person == null) {
            return null; 
        }
        
        return person.getTeacher(); 
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute("userId") != null;
    }

    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody Course course, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        Course createdCourse = courseService.createCourse(course, teacher);
        return ResponseEntity.ok(courseService.convertToDTO(createdCourse));
    }

    @PostMapping("/{courseId}/lessons")
    public ResponseEntity<Course> addLessonToCourse(
            @PathVariable Long courseId,
            @RequestBody Lesson lesson,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(courseService.addLessonToCourse(courseId, lesson));
    }

    @PostMapping("/{courseId}/quizzes")
    public ResponseEntity<Course> addQuizToCourse(
            @PathVariable Long courseId,
            @RequestBody Quiz quiz,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(courseService.addQuizToCourse(courseId, quiz));
    }

    @PutMapping("/{courseId}/lessons/{lessonId}/move-up")
    public ResponseEntity<Void> moveLessonUp(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        courseService.moveLessonUp(courseId, lessonId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{courseId}/lessons/{lessonId}/move-down")
    public ResponseEntity<Void> moveLessonDown(
            @PathVariable Long courseId,
            @PathVariable Long lessonId,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        courseService.moveLessonDown(courseId, lessonId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{courseId}/quizzes/{quizId}/move-up")
    public ResponseEntity<Void> moveQuizUp(
            @PathVariable Long courseId,
            @PathVariable Long quizId,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        courseService.moveQuizUp(courseId, quizId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{courseId}/quizzes/{quizId}/move-down")
    public ResponseEntity<Void> moveQuizDown(
            @PathVariable Long courseId,
            @PathVariable Long quizId,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        courseService.moveQuizDown(courseId, quizId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/my-courses")
    public ResponseEntity<List<CourseDTO>> getMyCourses(HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
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
            @RequestBody Course course,
            HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(courseService.updateCourse(id, course));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id, HttpServletRequest request) {
        if (!isAuthenticated(request)) {
            return ResponseEntity.status(401).build();
        }
        
        Teacher teacher = getAuthenticatedTeacher(request);
        if (teacher == null) {
            return ResponseEntity.status(403).build();
        }
        
        courseService.deleteCourse(id);
        return ResponseEntity.ok().build();
    }
} 
package mas.chrzanof.project.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.dto.CourseDTO;
import mas.chrzanof.project.dto.LessonDTO;
import mas.chrzanof.project.dto.QuizDTO;
import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Lesson;
import mas.chrzanof.project.model.Quiz;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.CourseRepository;
import mas.chrzanof.project.repository.LessonRepository;
import mas.chrzanof.project.repository.QuizRepository;

@Service
@Transactional
public class CourseService {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final QuizRepository quizRepository;

    public CourseService(CourseRepository courseRepository, LessonRepository lessonRepository, QuizRepository quizRepository) {
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.quizRepository = quizRepository;
    }

    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public CourseDTO convertToDTO(Course course) {
        CourseDTO dto = new CourseDTO();
        dto.setId(course.getId());
        dto.setTitle(course.getTitle());
        dto.setDescription(course.getDescription());
        dto.setAvailableFrom(course.getAvailableFrom());
        dto.setAvailableTo(course.getAvailableTo());
        

        Map<Integer, LessonDTO> lessonDTOs = new HashMap<>();
        course.getLessons().forEach((number, lesson) -> {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.setId(lesson.getId());
            lessonDTO.setTitle(lesson.getTitle());
            lessonDTO.setContent(lesson.getContent());
            lessonDTO.setDescription(lesson.getDescription());
            lessonDTO.setLessonNumber(lesson.getLessonNumber());
            lessonDTOs.put(number, lessonDTO);
        });
        dto.setLessons(lessonDTOs);
        
        
        List<QuizDTO> quizDTOs = course.getQuizzes().stream()
            .map(quiz -> {
                QuizDTO quizDTO = new QuizDTO();
                quizDTO.setId(quiz.getId());
                quizDTO.setTitle(quiz.getTitle());
                quizDTO.setDescription(quiz.getDescription());
                quizDTO.setPositionIndex(quiz.getPositionIndex());
                return quizDTO;
            })
            .collect(Collectors.toList());
        dto.setQuizzes(quizDTOs);
        
        return dto;
    }

    @Transactional
    public Course createCourse(Course course, Teacher teacher) {
        course.getAssignedTeachers().add(teacher);
        course.setTeacherInCharge(teacher);
        return courseRepository.save(course);
    }

    @Transactional
    public Course addLessonToCourse(Long courseId, Lesson lesson) {
        Course course = getCourseById(courseId);
        lesson.setCourse(course);
        
        int nextLessonNumber = course.getLessons().size() + 1;
        lesson.setLessonNumber(nextLessonNumber);
        
        course.getLessons().put(nextLessonNumber, lesson);
        lessonRepository.save(lesson);
        return course;
    }

    @Transactional
    public Course addQuizToCourse(Long courseId, Quiz quiz) {
        Course course = getCourseById(courseId);
        quiz.setCourse(course);
        quiz.getQuestions().forEach(question -> question.setQuiz(quiz));
        course.getQuizzes().add(quiz);
        quizRepository.save(quiz);
        return course;
    }

    public Course getCourseById(Long id) {
        return courseRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("Course not found with id: " + id));
    }

    @Transactional
    public Course updateCourse(Long id, Course updatedCourse) {
        Course existingCourse = getCourseById(id);
        existingCourse.setTitle(updatedCourse.getTitle());
        existingCourse.setDescription(updatedCourse.getDescription());
        existingCourse.setAvailableFrom(updatedCourse.getAvailableFrom());
        existingCourse.setAvailableTo(updatedCourse.getAvailableTo());
        return courseRepository.save(existingCourse);
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new NoSuchElementException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    @Transactional
    public Course assignTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        
        Course refreshedCourse = courseRepository.findById(course.getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        if (!refreshedCourse.getAssignedTeachers().contains(teacher)) {
            refreshedCourse.getAssignedTeachers().add(teacher);
            teacher.getAssignedCourses().add(refreshedCourse);
            return courseRepository.save(refreshedCourse);
        }
        return refreshedCourse;
    }

    @Transactional
    public Course removeTeacher(Course course, Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("Teacher cannot be null");
        }
        
        Course refreshedCourse = courseRepository.findById(course.getId())
            .orElseThrow(() -> new IllegalArgumentException("Course not found"));
        
        if (refreshedCourse.getAssignedTeachers().contains(teacher)) {
            refreshedCourse.getAssignedTeachers().remove(teacher);
            teacher.getAssignedCourses().remove(refreshedCourse);
            if (refreshedCourse.getTeacherInCharge() != null && refreshedCourse.getTeacherInCharge().equals(teacher)) {
                refreshedCourse.setTeacherInCharge(null);
            }
            return courseRepository.save(refreshedCourse);
        }
        return refreshedCourse;
    }

    @Transactional
    public Course setTeacherInCharge(Course course, Teacher teacher) {
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
        
        return courseRepository.save(refreshedCourse);
    }

    public boolean isTeacherAssigned(Course course, Teacher teacher) {
        return course.getAssignedTeachers().contains(teacher);
    }

    public boolean isTeacherInCharge(Course course, Teacher teacher) {
        return course.getTeacherInCharge() != null && course.getTeacherInCharge().equals(teacher);
    }

    public List<CourseDTO> getCoursesByTeacherInCharge(Teacher teacher) {
        return courseRepository.findCoursesByTeacherInChargeWithLessonsAndQuizzes(teacher).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void moveLessonUp(Long courseId, Long lessonId) {
        Course course = getCourseById(courseId);
        Lesson lessonToMove = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found with id: " + lessonId));
        
        int currentPosition = lessonToMove.getLessonNumber();
        if (currentPosition <= 1) {
            throw new IllegalStateException("Lesson is already at the top");
        }
        
        Lesson lessonAbove = course.getLessons().get(currentPosition - 1);
        if (lessonAbove == null) {
            throw new IllegalStateException("No lesson found at position " + (currentPosition - 1));
        }
        
        lessonToMove.setLessonNumber(currentPosition - 1);
        lessonAbove.setLessonNumber(currentPosition);
        
        lessonRepository.save(lessonToMove);
        lessonRepository.save(lessonAbove);
        
        course.getLessons().put(currentPosition - 1, lessonToMove);
        course.getLessons().put(currentPosition, lessonAbove);
    }

    @Transactional
    public void moveLessonDown(Long courseId, Long lessonId) {
        Course course = getCourseById(courseId);
        Lesson lessonToMove = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new NoSuchElementException("Lesson not found with id: " + lessonId));
        
        int currentPosition = lessonToMove.getLessonNumber();
        int maxPosition = course.getLessons().size();
        
        if (currentPosition >= maxPosition) {
            throw new IllegalStateException("Lesson is already at the bottom");
        }
        
        Lesson lessonBelow = course.getLessons().get(currentPosition + 1);
        if (lessonBelow == null) {
            throw new IllegalStateException("No lesson found at position " + (currentPosition + 1));
        }
        
        lessonToMove.setLessonNumber(currentPosition + 1);
        lessonBelow.setLessonNumber(currentPosition);
        
        lessonRepository.save(lessonToMove);
        lessonRepository.save(lessonBelow);
        
        course.getLessons().put(currentPosition + 1, lessonToMove);
        course.getLessons().put(currentPosition, lessonBelow);
    }
} 
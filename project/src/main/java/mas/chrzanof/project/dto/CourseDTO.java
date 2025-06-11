package mas.chrzanof.project.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class CourseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime availableFrom;
    private LocalDateTime availableTo;
    private Map<Integer, LessonDTO> lessons;
    private List<QuizDTO> quizzes;
} 
package mas.chrzanof.project.dto;

import lombok.Data;

@Data
public class LessonDTO {
    private Long id;
    private String title;
    private String content;
    private String description;
    private Integer lessonNumber;
} 
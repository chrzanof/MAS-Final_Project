package mas.chrzanof.project.dto;

import lombok.Data;

@Data
public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private Integer positionIndex;
} 
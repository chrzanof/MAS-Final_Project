package mas.chrzanof.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "open_questions")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class OpenQuestion extends Question {
    @Column(nullable = false)
    private String correctAnswer;
}

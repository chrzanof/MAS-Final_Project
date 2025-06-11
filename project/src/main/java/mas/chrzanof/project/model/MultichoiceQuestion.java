package mas.chrzanof.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "multichoice_questions")
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class MultichoiceQuestion extends Question {
    @ElementCollection
    @Column(name = "option")
    private List<String> options = new ArrayList<>();

    @Column(nullable = false)
    private Integer correctOptionIndex;
}

package mas.chrzanof.project.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "students")
@NoArgsConstructor
@Data
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String studentId;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false, updatable=false)
    private Person person;

    @OneToMany(mappedBy = "student")
    private List<FlashcardDeck> flashcardDecks = new ArrayList<>();

    @PostPersist
    public void generateStudentId() {
        if (this.studentId == null) {
            this.studentId = "s" + this.id;
        }
    }
}

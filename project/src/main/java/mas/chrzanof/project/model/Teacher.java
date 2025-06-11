package mas.chrzanof.project.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@Data
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teacherId;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false, updatable=false)
    private Person person;

    @PostPersist
    public void generateTeacherId() {
        if (this.teacherId == null) {
            this.teacherId = "t" + this.id;
        }
    }
}

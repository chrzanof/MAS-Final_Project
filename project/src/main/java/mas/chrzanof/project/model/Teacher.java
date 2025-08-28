package mas.chrzanof.project.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@NoArgsConstructor
@Getter
@Setter
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String teacherId;

    @OneToOne
    @JoinColumn(name = "person_id", nullable = false, updatable=false)
    private Person person;

    @ManyToMany
    @JoinTable(
        name = "course_teacher_assignment",
        joinColumns = @JoinColumn(name = "teacher_id"),
        inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> assignedCourses = new HashSet<>();

    @OneToMany(mappedBy = "teacherInCharge")
    private Set<Course> coursesInCharge = new HashSet<>();

    @PostPersist
    public void generateTeacherId() {
        if (this.teacherId == null) {
            this.teacherId = "t" + this.id;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) &&
               Objects.equals(teacherId, teacher.teacherId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, teacherId);
    }

    public void assignTo(Course course) {
        assignedCourses.add(course);
    }

    public void setInChargeOf(Course course) {
        if(!assignedCourses.contains(course)) {
            throw new IllegalArgumentException("This course is not assigned to a teacher!");
        }
        coursesInCharge.add(course);
    }
}

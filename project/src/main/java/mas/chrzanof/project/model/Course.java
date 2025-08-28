package mas.chrzanof.project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Getter
@Setter
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Column(name = "available_from", nullable = false)
    private LocalDateTime availableFrom;

    @NotNull
    @Column(name = "available_to", nullable = false)
    private LocalDateTime availableTo;

    @ManyToMany
    @JoinTable(
        name = "course_teacher_assignment",
        joinColumns = @JoinColumn(name = "course_id"),
        inverseJoinColumns = @JoinColumn(name = "teacher_id")
    )
    private Set<Teacher> assignedTeachers = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "teacher_in_charge_id")
    private Teacher teacherInCharge;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("positionIndex ASC")
    private Set<Quiz> quizzes = new LinkedHashSet<>();

    @OneToMany(mappedBy = "course")
    @MapKey(name = "lessonNumber")
    @OrderBy("lessonNumber ASC")
    private Map<Integer, Lesson> lessons = new HashMap<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Enrollment> enrollments = new ArrayList<>();

    public void addEnrollment(Enrollment enrollment) {
        enrollments.add(enrollment);
        enrollment.setCourse(this);
    }

    public void removeEnrollment(Enrollment enrollment) {
        enrollments.remove(enrollment);
        enrollment.setCourse(null);
    }
    public void setTeacherInCharge(Teacher teacher) {
        if(!assignedTeachers.contains(teacher)) {
            throw new IllegalArgumentException("Teacher must be assigned to the course!");
        }
        this.teacherInCharge = teacher;
    }
}

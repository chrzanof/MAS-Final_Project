package mas.chrzanof.project.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "enrollments", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"}))
@NoArgsConstructor
@Getter
@Setter
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    @Transient
    public Double getCompletionPercentage() {
        if (course == null || course.getQuizzes() == null || course.getQuizzes().isEmpty()) {
            return 0.0;
        }

        long totalQuestions = course.getQuizzes().stream()
            .flatMap(quiz -> quiz.getQuestions().stream())
            .count();

        if (totalQuestions == 0) {
            return 0.0;
        }

        long completedQuestions = answers.stream()
            .filter(Answer::getIsCompleted)
            .count();

        return (double) completedQuestions / totalQuestions * 100;
    }

    public Enrollment(Course course, Student student) {
        this.course = course;
        this.student = student;
        this.enrollmentDate = LocalDateTime.now();
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setEnrollment(this);
    }

    public void removeAnswer(Answer answer) {
        answers.remove(answer);
        answer.setEnrollment(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(course, that.course) &&
               Objects.equals(student, that.student) &&
               Objects.equals(enrollmentDate, that.enrollmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, student, enrollmentDate);
    }
} 
package mas.chrzanof.project.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@Getter
@Setter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String surname;
    private String email;
    private Integer phoneNumber;
    private String password;
    
    @ElementCollection
    @CollectionTable(name = "person_languages", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "language")
    private List<String> languagesKnown = new ArrayList<>();
    
    @ManyToMany(mappedBy = "people")
    private List<Chat> chats = new ArrayList<>();
    
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Teacher teacher;

    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Student student;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(id, person.id) &&
               Objects.equals(name, person.name) &&
               Objects.equals(surname, person.surname) &&
               Objects.equals(email, person.email) &&
               Objects.equals(phoneNumber, person.phoneNumber) &&
               Objects.equals(password, person.password) &&
               Objects.equals(languagesKnown, person.languagesKnown);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surname, email, phoneNumber, password, languagesKnown);
    }
}

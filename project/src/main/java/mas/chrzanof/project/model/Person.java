package mas.chrzanof.project.model;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "persons")
@NoArgsConstructor
@Data
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
}

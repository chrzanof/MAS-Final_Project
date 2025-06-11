package mas.chrzanof.project;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mas.chrzanof.project.model.Flashcard;
import mas.chrzanof.project.model.FlashcardDeck;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.model.enums.State;
import mas.chrzanof.project.repository.FlashcardDeckRepository;
import mas.chrzanof.project.repository.FlashcardRepository;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.repository.StudentRepository;
import mas.chrzanof.project.repository.TeacherRepository;

@Configuration
public class Runner {

    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository, 
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository,
                                 FlashcardDeckRepository flashcardDeckRepository,
                                 FlashcardRepository flashcardRepository) {
        return args -> {
            // Create persons
            Person john = new Person();
            john.setName("John");
            john.setSurname("Doe");
            john.setEmail("john.doe@example.com");
            john.setPhoneNumber(123456789);
            john.setPassword("password123");
            john.setLanguagesKnown(Arrays.asList("English", "Spanish"));
            personRepository.save(john);

            Person alice = new Person();
            alice.setName("Alice");
            alice.setSurname("Smith");
            alice.setEmail("alice.smith@example.com");
            alice.setPhoneNumber(987654321);
            alice.setPassword("password456");
            alice.setLanguagesKnown(Arrays.asList("English", "French", "German"));
            personRepository.save(alice);

            Person bob = new Person();
            bob.setName("Bob");
            bob.setSurname("Johnson");
            bob.setEmail("bob.johnson@example.com");
            bob.setPhoneNumber(555555555);
            bob.setPassword("password789");
            bob.setLanguagesKnown(Arrays.asList("English", "Polish"));
            personRepository.save(bob);

            // Create teachers
            Teacher teacher1 = new Teacher();
            teacher1.setPerson(john);
            teacherRepository.save(teacher1);

            Teacher teacher2 = new Teacher();
            teacher2.setPerson(alice);
            teacherRepository.save(teacher2);

            // Create students
            Student student1 = new Student();
            student1.setPerson(alice); // Alice is both a teacher and a student
            studentRepository.save(student1);

            Student student2 = new Student();
            student2.setPerson(bob);
            studentRepository.save(student2);

            // Update person references
            john.setTeacher(teacher1);
            alice.setTeacher(teacher2);
            alice.setStudent(student1);
            bob.setStudent(student2);

            personRepository.saveAll(Arrays.asList(john, alice, bob));

            // Create flashcard decks
            FlashcardDeck englishDeck = new FlashcardDeck();
            englishDeck.setTitle("English Vocabulary");
            englishDeck.setDescription("Basic English vocabulary for beginners");
            englishDeck.setStudent(student1);
            flashcardDeckRepository.save(englishDeck);

            FlashcardDeck frenchDeck = new FlashcardDeck();
            frenchDeck.setTitle("French Basics");
            frenchDeck.setDescription("Essential French phrases and vocabulary");
            frenchDeck.setStudent(student1);
            flashcardDeckRepository.save(frenchDeck);

            FlashcardDeck germanDeck = new FlashcardDeck();
            germanDeck.setTitle("German Grammar");
            germanDeck.setDescription("German grammar rules and examples");
            germanDeck.setStudent(student2);
            flashcardDeckRepository.save(germanDeck);

            // Create flashcards
            Flashcard card1 = new Flashcard();
            card1.setQuestion("What is the past tense of 'go'?");
            card1.setAnswer("went");
            card1.setState(State.NEW);
            card1.setFlashcardDeck(englishDeck);
            flashcardRepository.save(card1);

            Flashcard card2 = new Flashcard();
            card2.setQuestion("How do you say 'Hello' in French?");
            card2.setAnswer("Bonjour");
            card2.setState(State.LEARNING);
            card2.setFlashcardDeck(frenchDeck);
            flashcardRepository.save(card2);

            Flashcard card3 = new Flashcard();
            card3.setQuestion("What is the meaning of 'ubiquitous'?");
            card3.setAnswer("Present, appearing, or found everywhere");
            card3.setState(State.REVIEW);
            card3.setFlashcardDeck(englishDeck);
            flashcardRepository.save(card3);

            Flashcard card4 = new Flashcard();
            card4.setQuestion("What is the German word for 'book'?");
            card4.setAnswer("Buch");
            card4.setState(State.NEW);
            card4.setFlashcardDeck(germanDeck);
            flashcardRepository.save(card4);

            Flashcard card5 = new Flashcard();
            card5.setQuestion("How do you say 'Thank you' in French?");
            card5.setAnswer("Merci");
            card5.setState(State.LEARNING);
            card5.setFlashcardDeck(frenchDeck);
            flashcardRepository.save(card5);
        };
    }
}

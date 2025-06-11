package mas.chrzanof.project;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.repository.StudentRepository;
import mas.chrzanof.project.repository.TeacherRepository;

@Configuration
public class Runner {

    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository, 
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository) {
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
            teacher1.setTeacherId(1L);
            teacher1.setPerson(john);
            teacherRepository.save(teacher1);

            Teacher teacher2 = new Teacher();
            teacher2.setTeacherId(2L);
            teacher2.setPerson(alice);
            teacherRepository.save(teacher2);

            // Create students
            Student student1 = new Student();
            student1.setStudentId(1L);
            student1.setPerson(alice); // Alice is both a teacher and a student
            studentRepository.save(student1);

            Student student2 = new Student();
            student2.setStudentId(2L);
            student2.setPerson(bob);
            studentRepository.save(student2);

            // Update person references
            john.setTeacher(teacher1);
            alice.setTeacher(teacher2);
            alice.setStudent(student1);
            bob.setStudent(student2);

            personRepository.saveAll(Arrays.asList(john, alice, bob));
        };
    }
}

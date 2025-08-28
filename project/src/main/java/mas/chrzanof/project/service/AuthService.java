package mas.chrzanof.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mas.chrzanof.project.dto.LoginRequest;
import mas.chrzanof.project.dto.RegisterRequest;
import mas.chrzanof.project.dto.UserResponse;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.repository.StudentRepository;
import mas.chrzanof.project.repository.TeacherRepository;

@Service
public class AuthService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse login(LoginRequest loginRequest) {
        Optional<Person> personOpt = personRepository.findByEmail(loginRequest.getEmail());
        
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), person.getPassword())) {
                boolean isTeacher = teacherRepository.findByPersonId(person.getId()) != null;
                boolean isStudent = studentRepository.findByPersonId(person.getId()) != null;
                
                return new UserResponse(
                    person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getEmail(),
                    person.getPhoneNumber(),
                    isTeacher,
                    isStudent
                );
            }
        }
        return null;
    }

    @Transactional
    public UserResponse register(RegisterRequest registerRequest) {
        if (personRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        System.out.println("Registration request - isTeacher: " + registerRequest.getIsTeacher() + ", isStudent: " + registerRequest.getIsStudent());

        Person person = new Person();
        person.setName(registerRequest.getName());
        person.setSurname(registerRequest.getSurname());
        person.setEmail(registerRequest.getEmail());
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        person.setPhoneNumber(registerRequest.getPhoneNumber());

        Person savedPerson = personRepository.save(person);
        System.out.println("Person saved with ID: " + savedPerson.getId());

        if (registerRequest.getIsTeacher()) {
            System.out.println("Creating Teacher entity...");
            Teacher teacher = new Teacher();
            teacher.setPerson(savedPerson);
            Teacher savedTeacher = teacherRepository.save(teacher);
            System.out.println("Teacher saved with ID: " + savedTeacher.getId());
            savedPerson.setTeacher(savedTeacher);
            savedPerson = personRepository.save(savedPerson);
        }

        if (registerRequest.getIsStudent()) {
            System.out.println("Creating Student entity...");
            Student student = new Student();
            student.setPerson(savedPerson);
            Student savedStudent = studentRepository.save(student);
            System.out.println("Student saved with ID: " + savedStudent.getId());
            savedPerson.setStudent(savedStudent);
            savedPerson = personRepository.save(savedPerson);
        }

        boolean isTeacher = teacherRepository.findByPersonId(savedPerson.getId()) != null;
        boolean isStudent = studentRepository.findByPersonId(savedPerson.getId()) != null;
        
        System.out.println("Final check - isTeacher: " + isTeacher + ", isStudent: " + isStudent);
        
        return new UserResponse(
            savedPerson.getId(),
            savedPerson.getName(),
            savedPerson.getSurname(),
            savedPerson.getEmail(),
            savedPerson.getPhoneNumber(),
            isTeacher,
            isStudent
        );
    }

    public UserResponse getCurrentUser(Long userId) {
        Optional<Person> personOpt = personRepository.findById(userId);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();

            boolean isTeacher = teacherRepository.findByPersonId(person.getId()) != null;
            boolean isStudent = studentRepository.findByPersonId(person.getId()) != null;
            
            return new UserResponse(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                person.getPhoneNumber(),
                isTeacher,
                isStudent
            );
        }
        return null;
    }
}

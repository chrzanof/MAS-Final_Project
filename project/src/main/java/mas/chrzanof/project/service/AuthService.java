package mas.chrzanof.project.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import mas.chrzanof.project.dto.LoginRequest;
import mas.chrzanof.project.dto.RegisterRequest;
import mas.chrzanof.project.dto.UserResponse;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.repository.PersonRepository;

@Service
public class AuthService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse login(LoginRequest loginRequest) {
        Optional<Person> personOpt = personRepository.findByEmail(loginRequest.getEmail());
        
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), person.getPassword())) {
                return new UserResponse(
                    person.getId(),
                    person.getName(),
                    person.getSurname(),
                    person.getEmail(),
                    person.getPhoneNumber()
                );
            }
        }
        return null;
    }

    public UserResponse register(RegisterRequest registerRequest) {
        if (personRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Person person = new Person();
        person.setName(registerRequest.getName());
        person.setSurname(registerRequest.getSurname());
        person.setEmail(registerRequest.getEmail());
        person.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        person.setPhoneNumber(registerRequest.getPhoneNumber());

        Person savedPerson = personRepository.save(person);

        return new UserResponse(
            savedPerson.getId(),
            savedPerson.getName(),
            savedPerson.getSurname(),
            savedPerson.getEmail(),
            savedPerson.getPhoneNumber()
        );
    }

    public UserResponse getCurrentUser(Long userId) {
        Optional<Person> personOpt = personRepository.findById(userId);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            return new UserResponse(
                person.getId(),
                person.getName(),
                person.getSurname(),
                person.getEmail(),
                person.getPhoneNumber()
            );
        }
        return null;
    }
}

package mas.chrzanof.project;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Flashcard;
import mas.chrzanof.project.model.FlashcardDeck;
import mas.chrzanof.project.model.Lesson;
import mas.chrzanof.project.model.MultichoiceQuestion;
import mas.chrzanof.project.model.OpenQuestion;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Quiz;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.model.enums.State;
import mas.chrzanof.project.repository.CourseRepository;
import mas.chrzanof.project.repository.FlashcardDeckRepository;
import mas.chrzanof.project.repository.FlashcardRepository;
import mas.chrzanof.project.repository.LessonRepository;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.repository.QuestionRepository;
import mas.chrzanof.project.repository.QuizRepository;
import mas.chrzanof.project.repository.StudentRepository;
import mas.chrzanof.project.repository.TeacherRepository;
import mas.chrzanof.project.service.CourseService;

@Configuration
public class Runner {

    @Bean
    CommandLineRunner initDatabase(PersonRepository personRepository, 
                                 TeacherRepository teacherRepository,
                                 StudentRepository studentRepository,
                                 FlashcardDeckRepository flashcardDeckRepository,
                                 FlashcardRepository flashcardRepository,
                                 QuizRepository quizRepository,
                                 QuestionRepository questionRepository,
                                 CourseRepository courseRepository,
                                 LessonRepository lessonRepository,
                                 CourseService courseService) {
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

            // Create courses
            Course englishCourse = new Course();
            englishCourse.setTitle("English Language Course");
            englishCourse.setDescription("Comprehensive English language course");
            englishCourse.setAvailableFrom(LocalDateTime.now());
            englishCourse.setAvailableTo(LocalDateTime.now().plusMonths(3));
            courseRepository.save(englishCourse);

            // Assign teachers to the course
            courseService.assignTeacher(englishCourse, teacher1);
            courseService.assignTeacher(englishCourse, teacher2);

            // Set teacher in charge of the course
            courseService.setTeacherInCharge(englishCourse, teacher1);

            // Create lessons
            Lesson lesson1 = new Lesson();
            lesson1.setTitle("Introduction to English Grammar");
            lesson1.setContent("In this lesson, we will cover the basics of English grammar, including parts of speech and sentence structure.");
            lesson1.setLessonNumber(1);
            lesson1.setCourse(englishCourse);
            lessonRepository.save(lesson1);

            Lesson lesson2 = new Lesson();
            lesson2.setTitle("Present Tense Verbs");
            lesson2.setContent("Learn about different forms of present tense verbs and how to use them correctly in sentences.");
            lesson2.setLessonNumber(2);
            lesson2.setCourse(englishCourse);
            lessonRepository.save(lesson2);

            Lesson lesson3 = new Lesson();
            lesson3.setTitle("Past Tense Verbs");
            lesson3.setContent("Understanding regular and irregular past tense verbs, and when to use them.");
            lesson3.setLessonNumber(3);
            lesson3.setCourse(englishCourse);
            lessonRepository.save(lesson3);

            // Create quizzes
            Quiz englishQuiz = new Quiz();
            englishQuiz.setTitle("English Grammar Quiz");
            englishQuiz.setDescription("Test your knowledge of English grammar");
            englishQuiz.setPositionIndex(0);
            englishQuiz.setCourse(englishCourse);
            quizRepository.save(englishQuiz);

            // Add multichoice questions
            MultichoiceQuestion q1 = new MultichoiceQuestion();
            q1.setText("Choose the correct past tense of 'go':");
            q1.setPoints(2);
            q1.setPositionIndex(0);
            q1.setQuiz(englishQuiz);
            q1.setOptions(Arrays.asList("goed", "went", "gone", "going"));
            q1.setCorrectOptionIndex(1);
            questionRepository.save(q1);

            MultichoiceQuestion q2 = new MultichoiceQuestion();
            q2.setText("Which word is a synonym for 'happy'?");
            q2.setPoints(1);
            q2.setPositionIndex(1);
            q2.setQuiz(englishQuiz);
            q2.setOptions(Arrays.asList("sad", "joyful", "angry", "tired"));
            q2.setCorrectOptionIndex(1);
            questionRepository.save(q2);

            // Add open questions
            OpenQuestion q3 = new OpenQuestion();
            q3.setText("What is the past participle of 'write'?");
            q3.setPoints(2);
            q3.setPositionIndex(2);
            q3.setQuiz(englishQuiz);
            q3.setCorrectAnswer("written");
            questionRepository.save(q3);

            OpenQuestion q4 = new OpenQuestion();
            q4.setText("Define the word 'ubiquitous'.");
            q4.setPoints(3);
            q4.setPositionIndex(3);
            q4.setQuiz(englishQuiz);
            q4.setCorrectAnswer("Present, appearing, or found everywhere");
            questionRepository.save(q4);

            // Create another quiz
            Quiz vocabularyQuiz = new Quiz();
            vocabularyQuiz.setTitle("Vocabulary Quiz");
            vocabularyQuiz.setDescription("Test your English vocabulary");
            vocabularyQuiz.setPositionIndex(1);
            vocabularyQuiz.setCourse(englishCourse);
            quizRepository.save(vocabularyQuiz);

            MultichoiceQuestion q5 = new MultichoiceQuestion();
            q5.setText("What is the meaning of 'ephemeral'?");
            q5.setPoints(2);
            q5.setPositionIndex(0);
            q5.setQuiz(vocabularyQuiz);
            q5.setOptions(Arrays.asList("lasting forever", "lasting for a very short time", "extremely large", "extremely small"));
            q5.setCorrectOptionIndex(1);
            questionRepository.save(q5);

            OpenQuestion q6 = new OpenQuestion();
            q6.setText("Use the word 'serendipity' in a sentence.");
            q6.setPoints(4);
            q6.setPositionIndex(1);
            q6.setQuiz(vocabularyQuiz);
            q6.setCorrectAnswer("It was pure serendipity that I found my lost book in the library.");
            questionRepository.save(q6);
        };
    }
}

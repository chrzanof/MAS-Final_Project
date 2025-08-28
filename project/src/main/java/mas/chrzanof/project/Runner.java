package mas.chrzanof.project;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import mas.chrzanof.project.model.Answer;
import mas.chrzanof.project.model.Chat;
import mas.chrzanof.project.model.Course;
import mas.chrzanof.project.model.Enrollment;
import mas.chrzanof.project.model.Flashcard;
import mas.chrzanof.project.model.FlashcardDeck;
import mas.chrzanof.project.model.Lesson;
import mas.chrzanof.project.model.Message;
import mas.chrzanof.project.model.MultichoiceQuestion;
import mas.chrzanof.project.model.OpenQuestion;
import mas.chrzanof.project.model.Person;
import mas.chrzanof.project.model.Quiz;
import mas.chrzanof.project.model.Student;
import mas.chrzanof.project.model.Teacher;
import mas.chrzanof.project.model.enums.State;
import mas.chrzanof.project.repository.ChatRepository;
import mas.chrzanof.project.repository.CourseRepository;
import mas.chrzanof.project.repository.FlashcardDeckRepository;
import mas.chrzanof.project.repository.FlashcardRepository;
import mas.chrzanof.project.repository.LessonRepository;
import mas.chrzanof.project.repository.MessageRepository;
import mas.chrzanof.project.repository.PersonRepository;
import mas.chrzanof.project.repository.QuestionRepository;
import mas.chrzanof.project.repository.QuizRepository;
import mas.chrzanof.project.repository.StudentRepository;
import mas.chrzanof.project.repository.TeacherRepository;
import mas.chrzanof.project.service.CourseService;
import mas.chrzanof.project.service.EnrollmentService;

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
                                 ChatRepository chatRepository,
                                 MessageRepository messageRepository,
                                 CourseService courseService,
                                 EnrollmentService enrollmentService,
                                 PasswordEncoder passwordEncoder) {
        return args -> {
            
            // Create people
            Person sarah = new Person();
            sarah.setName("Sarah");
            sarah.setSurname("Johnson");
            sarah.setEmail("sarah.johnson@example.com");
            sarah.setPhoneNumber(123456789);
            sarah.setPassword(passwordEncoder.encode("password123"));
            sarah.setLanguagesKnown(Arrays.asList("English", "Spanish", "German"));
            personRepository.save(sarah);

            Person michael = new Person();
            michael.setName("Michael");
            michael.setSurname("Brown");
            michael.setEmail("michael.brown@example.com");
            michael.setPhoneNumber(987654321);
            michael.setPassword(passwordEncoder.encode("password456"));
            michael.setLanguagesKnown(Arrays.asList("English", "French", "Italian"));
            personRepository.save(michael);

            Person emma = new Person();
            emma.setName("Emma");
            emma.setSurname("Davis");
            emma.setEmail("emma.davis@example.com");
            emma.setPhoneNumber(555555555);
            emma.setPassword(passwordEncoder.encode("password789"));
            emma.setLanguagesKnown(Arrays.asList("English", "German", "Dutch"));
            personRepository.save(emma);

            Person james = new Person();
            james.setName("James");
            james.setSurname("Wilson");
            james.setEmail("james.wilson@example.com");
            james.setPhoneNumber(111222333);
            james.setPassword(passwordEncoder.encode("password321"));
            james.setLanguagesKnown(Arrays.asList("English", "Spanish"));
            personRepository.save(james);

            Person olivia = new Person();
            olivia.setName("Olivia");
            olivia.setSurname("Miller");
            olivia.setEmail("olivia.miller@example.com");
            olivia.setPhoneNumber(444555666);
            olivia.setPassword(passwordEncoder.encode("password654"));
            olivia.setLanguagesKnown(Arrays.asList("English", "French", "Portuguese"));
            personRepository.save(olivia);

            
            // Create teachers
            Teacher teacher1 = new Teacher();
            teacher1.setPerson(sarah);
            teacherRepository.save(teacher1);

            Teacher teacher2 = new Teacher();
            teacher2.setPerson(michael);
            teacherRepository.save(teacher2);

            Teacher teacher3 = new Teacher();
            teacher3.setPerson(emma);
            teacherRepository.save(teacher3);

            // Create students
            Student student1 = new Student();
            student1.setPerson(emma);
            studentRepository.save(student1);

            Student student2 = new Student();
            student2.setPerson(james);
            studentRepository.save(student2);

            Student student3 = new Student();
            student3.setPerson(olivia);
            studentRepository.save(student3);

            // Link teachers and students to persons
            sarah.setTeacher(teacher1);
            michael.setTeacher(teacher2);
            emma.setTeacher(teacher3);
            emma.setStudent(student1);
            james.setStudent(student2);
            olivia.setStudent(student3);

            personRepository.saveAll(Arrays.asList(sarah, michael, emma, james, olivia));

            
            // Create flashcard decks
            FlashcardDeck englishDeck = new FlashcardDeck();
            englishDeck.setTitle("Advanced English Vocabulary");
            englishDeck.setDescription("Advanced English vocabulary for upper-intermediate learners");
            englishDeck.setStudent(student1);
            flashcardDeckRepository.save(englishDeck);

            FlashcardDeck germanDeck = new FlashcardDeck();
            germanDeck.setTitle("German Basics");
            germanDeck.setDescription("Essential German phrases and vocabulary for beginners");
            germanDeck.setStudent(student2);
            flashcardDeckRepository.save(germanDeck);

            FlashcardDeck spanishDeck = new FlashcardDeck();
            spanishDeck.setTitle("Spanish Grammar");
            spanishDeck.setDescription("Spanish grammar rules and examples");
            spanishDeck.setStudent(student3);
            flashcardDeckRepository.save(spanishDeck);

            FlashcardDeck frenchDeck = new FlashcardDeck();
            frenchDeck.setTitle("Business French");
            frenchDeck.setDescription("Professional French vocabulary and expressions");
            frenchDeck.setStudent(student1);
            flashcardDeckRepository.save(frenchDeck);

            
            // Create flashcards for English deck
            Flashcard card1 = new Flashcard();
            card1.setQuestion("What does 'ubiquitous' mean?");
            card1.setAnswer("Present, appearing, or found everywhere");
            card1.setState(State.NEW);
            card1.setFlashcardDeck(englishDeck);
            flashcardRepository.save(card1);

            Flashcard card2 = new Flashcard();
            card2.setQuestion("What does 'serendipity' mean?");
            card2.setAnswer("A pleasant surprise; a fortunate accident");
            card2.setState(State.REVIEW);
            card2.setFlashcardDeck(englishDeck);
            flashcardRepository.save(card2);

            Flashcard card3 = new Flashcard();
            card3.setQuestion("What is the past participle of 'write'?");
            card3.setAnswer("written");
            card3.setState(State.LEARNING);
            card3.setFlashcardDeck(englishDeck);
            flashcardRepository.save(card3);

            // Create flashcards for German deck
            Flashcard card4 = new Flashcard();
            card4.setQuestion("What is 'book' in German?");
            card4.setAnswer("das Buch");
            card4.setState(State.NEW);
            card4.setFlashcardDeck(germanDeck);
            flashcardRepository.save(card4);

            Flashcard card5 = new Flashcard();
            card5.setQuestion("How do you say 'Good day' in German?");
            card5.setAnswer("Guten Tag");
            card5.setState(State.LEARNING);
            card5.setFlashcardDeck(germanDeck);
            flashcardRepository.save(card5);

            Flashcard card6 = new Flashcard();
            card6.setQuestion("How do you say 'Thank you' in German?");
            card6.setAnswer("Danke schön");
            card6.setState(State.REVIEW);
            card6.setFlashcardDeck(germanDeck);
            flashcardRepository.save(card6);

            // Create flashcards for Spanish deck
            Flashcard card7 = new Flashcard();
            card7.setQuestion("What is 'house' in Spanish?");
            card7.setAnswer("la casa");
            card7.setState(State.NEW);
            card7.setFlashcardDeck(spanishDeck);
            flashcardRepository.save(card7);

            Flashcard card8 = new Flashcard();
            card8.setQuestion("How do you conjugate 'ser' in first person?");
            card8.setAnswer("soy");
            card8.setState(State.LEARNING);
            card8.setFlashcardDeck(spanishDeck);
            flashcardRepository.save(card8);

            // Create flashcards for French deck
            Flashcard card9 = new Flashcard();
            card9.setQuestion("What does 'néanmoins' mean in French?");
            card9.setAnswer("nevertheless, however");
            card9.setState(State.REVIEW);
            card9.setFlashcardDeck(frenchDeck);
            flashcardRepository.save(card9);

            Flashcard card10 = new Flashcard();
            card10.setQuestion("How do you say 'excuse me' formally in French?");
            card10.setAnswer("Je vous prie de m'excuser");
            card10.setState(State.NEW);
            card10.setFlashcardDeck(frenchDeck);
            flashcardRepository.save(card10);

            
            // Create courses
            Course englishCourse = new Course();
            englishCourse.setTitle("Advanced English Language Course");
            englishCourse.setDescription("Comprehensive English language course for intermediate to advanced learners");
            englishCourse.setAvailableFrom(LocalDateTime.now());
            englishCourse.setAvailableTo(LocalDateTime.now().plusMonths(4));
            courseRepository.save(englishCourse);

            Course germanCourse = new Course();
            germanCourse.setTitle("German for Beginners");
            germanCourse.setDescription("Learn German from scratch - basic communication skills");
            germanCourse.setAvailableFrom(LocalDateTime.now());
            germanCourse.setAvailableTo(LocalDateTime.now().plusMonths(6));
            courseRepository.save(germanCourse);

            Course frenchCourse = new Course();
            frenchCourse.setTitle("Business French");
            frenchCourse.setDescription("French language for business and professional communication");
            frenchCourse.setAvailableFrom(LocalDateTime.now().plusDays(7));
            frenchCourse.setAvailableTo(LocalDateTime.now().plusMonths(5));
            courseRepository.save(frenchCourse);

            // Assign teachers to courses
            courseService.assignTeacher(englishCourse, teacher1);
            courseService.assignTeacher(englishCourse, teacher2);
            courseService.assignTeacher(germanCourse, teacher2);
            courseService.assignTeacher(frenchCourse, teacher3);

            // Set teachers in charge
            courseService.setTeacherInCharge(englishCourse, teacher1);
            courseService.setTeacherInCharge(germanCourse, teacher2);
            courseService.setTeacherInCharge(frenchCourse, teacher3);

            
            // Create quizzes for English course
            Quiz englishQuiz1 = new Quiz();
            englishQuiz1.setTitle("English Grammar Quiz");
            englishQuiz1.setDescription("Test your knowledge of English grammar");
            englishQuiz1.setPositionIndex(0);
            englishQuiz1.setCourse(englishCourse);
            quizRepository.save(englishQuiz1);

            Quiz englishQuiz2 = new Quiz();
            englishQuiz2.setTitle("Vocabulary Quiz");
            englishQuiz2.setDescription("Test your English vocabulary knowledge");
            englishQuiz2.setPositionIndex(1);
            englishQuiz2.setCourse(englishCourse);
            quizRepository.save(englishQuiz2);

            // Create questions for first English quiz
            MultichoiceQuestion q1 = new MultichoiceQuestion();
            q1.setText("Choose the correct past tense of 'go':");
            q1.setPoints(2);
            q1.setPositionIndex(0);
            q1.setQuiz(englishQuiz1);
            q1.setOptions(Arrays.asList("goed", "went", "gone", "going"));
            q1.setCorrectOptionIndex(1);
            questionRepository.save(q1);

            MultichoiceQuestion q2 = new MultichoiceQuestion();
            q2.setText("Which word is a synonym for 'happy'?");
            q2.setPoints(1);
            q2.setPositionIndex(1);
            q2.setQuiz(englishQuiz1);
            q2.setOptions(Arrays.asList("sad", "joyful", "angry", "tired"));
            q2.setCorrectOptionIndex(1);
            questionRepository.save(q2);

            OpenQuestion q3 = new OpenQuestion();
            q3.setText("What is the past participle of 'write'?");
            q3.setPoints(2);
            q3.setPositionIndex(2);
            q3.setQuiz(englishQuiz1);
            q3.setCorrectAnswer("written");
            questionRepository.save(q3);

            // Create questions for second English quiz
            OpenQuestion q4 = new OpenQuestion();
            q4.setText("Define the word 'ubiquitous':");
            q4.setPoints(3);
            q4.setPositionIndex(0);
            q4.setQuiz(englishQuiz2);
            q4.setCorrectAnswer("Present, appearing, or found everywhere");
            questionRepository.save(q4);

            MultichoiceQuestion q5 = new MultichoiceQuestion();
            q5.setText("What does 'serendipity' mean?");
            q5.setPoints(2);
            q5.setPositionIndex(1);
            q5.setQuiz(englishQuiz2);
            q5.setOptions(Arrays.asList("a pleasant surprise", "a sad event", "a difficult situation", "an ordinary day"));
            q5.setCorrectOptionIndex(0);
            questionRepository.save(q5);

            // Create quizzes for German course
            Quiz germanQuiz1 = new Quiz();
            germanQuiz1.setTitle("German Basics Quiz");
            germanQuiz1.setDescription("Test your basic knowledge of German language");
            germanQuiz1.setPositionIndex(0);
            germanQuiz1.setCourse(germanCourse);
            quizRepository.save(germanQuiz1);

            MultichoiceQuestion qg1 = new MultichoiceQuestion();
            qg1.setText("What is the definite article for 'Buch' (book) in German?");
            qg1.setPoints(1);
            qg1.setPositionIndex(0);
            qg1.setQuiz(germanQuiz1);
            qg1.setOptions(Arrays.asList("der", "die", "das", "den"));
            qg1.setCorrectOptionIndex(2);
            questionRepository.save(qg1);

            OpenQuestion qg2 = new OpenQuestion();
            qg2.setText("How do you say 'Nice to meet you' in German?");
            qg2.setPoints(2);
            qg2.setPositionIndex(1);
            qg2.setQuiz(germanQuiz1);
            qg2.setCorrectAnswer("Freut mich, Sie kennenzulernen");
            questionRepository.save(qg2);

            // Create quiz for French course
            Quiz frenchQuiz1 = new Quiz();
            frenchQuiz1.setTitle("Business French Quiz");
            frenchQuiz1.setDescription("Test your knowledge of French in business context");
            frenchQuiz1.setPositionIndex(0);
            frenchQuiz1.setCourse(frenchCourse);
            quizRepository.save(frenchQuiz1);

            MultichoiceQuestion qf1 = new MultichoiceQuestion();
            qf1.setText("How do you say 'meeting' in French?");
            qf1.setPoints(1);
            qf1.setPositionIndex(0);
            qf1.setQuiz(frenchQuiz1);
            qf1.setOptions(Arrays.asList("la réunion", "le bureau", "la lettre", "le téléphone"));
            qf1.setCorrectOptionIndex(0);
            questionRepository.save(qf1);


            // Create enrollments
            Enrollment jamesEnrollment = enrollmentService.enrollStudent(englishCourse, student2);
            Enrollment oliviaEnrollment = enrollmentService.enrollStudent(englishCourse, student3);
            Enrollment emmaEnrollment = enrollmentService.enrollStudent(germanCourse, student1);

            // Create answers for James's enrollment in English course
            Answer answer1 = new Answer(jamesEnrollment, q1, "went");
            answer1.setIsCompleted(true);
            answer1.setIsCorrect(true);
            answer1.getNotes().add("Remembered from previous lesson");
            jamesEnrollment.addAnswer(answer1);

            Answer answer2 = new Answer(jamesEnrollment, q2, "sad");
            answer2.setIsCompleted(true);
            answer2.setIsCorrect(false);
            answer2.getNotes().add("Confused with antonym");
            jamesEnrollment.addAnswer(answer2);

            Answer answer3 = new Answer(jamesEnrollment, q3, "wrote");
            answer3.setIsCompleted(true);
            answer3.setIsCorrect(false);
            answer3.getNotes().add("Used simple past instead of past participle");
            jamesEnrollment.addAnswer(answer3);

            // Create answers for Olivia's enrollment
            Answer answer4 = new Answer(oliviaEnrollment, q4, "Present, appearing, or found everywhere");
            answer4.setIsCompleted(true);
            answer4.setIsCorrect(true);
            answer4.getNotes().add("Learned from flashcards");
            answer4.getNotes().add("Used in context: 'Smartphones have become ubiquitous in modern society'");
            oliviaEnrollment.addAnswer(answer4);

            Answer answer5 = new Answer(oliviaEnrollment, q5, "a pleasant surprise");
            answer5.setIsCompleted(true);
            answer5.setIsCorrect(true);
            answer5.getNotes().add("Excellent answer");
            oliviaEnrollment.addAnswer(answer5);

            // Create answers for Emma's German course enrollment
            Answer answer6 = new Answer(emmaEnrollment, qg1, "das");
            answer6.setIsCompleted(true);
            answer6.setIsCorrect(true);
            answer6.getNotes().add("Correctly identified neuter gender");
            emmaEnrollment.addAnswer(answer6);

            // Save enrollments
            enrollmentService.saveEnrollment(jamesEnrollment);
            enrollmentService.saveEnrollment(oliviaEnrollment);
            enrollmentService.saveEnrollment(emmaEnrollment);

            
            // Create lessons for English course
            Lesson lesson1 = new Lesson();
            lesson1.setTitle("Introduction to English Grammar");
            lesson1.setDescription("Learn the basics of English grammar, parts of speech and sentence structure.");
            lesson1.setContent("English grammar consists of several fundamental elements. The main parts of speech are: nouns, verbs, adjectives, adverbs, prepositions, pronouns, conjunctions, and interjections. Understanding these building blocks is essential for effective communication.");
            lesson1.setLessonNumber(1);
            lesson1.setCourse(englishCourse);
            lessonRepository.save(lesson1);

            Lesson lesson2 = new Lesson();
            lesson2.setTitle("Present Tense Verbs");
            lesson2.setDescription("Learn about different forms of present tense verbs and how to use them correctly in sentences.");
            lesson2.setContent("The present tense in English has several forms: Present Simple (I work), Present Continuous (I am working), Present Perfect (I have worked), Present Perfect Continuous (I have been working). Each form has different uses and contexts.");
            lesson2.setLessonNumber(2);
            lesson2.setCourse(englishCourse);
            lessonRepository.save(lesson2);

            Lesson lesson3 = new Lesson();
            lesson3.setTitle("Past Tense Verbs");
            lesson3.setDescription("Understanding regular and irregular past tense verbs, and when to use them.");
            lesson3.setContent("Past tense is divided into regular verbs (add -ed: work → worked) and irregular verbs (go → went, see → saw). Past Simple is used for actions completed in the past at a specific time.");
            lesson3.setLessonNumber(3);
            lesson3.setCourse(englishCourse);
            lessonRepository.save(lesson3);

            // Create lessons for German course
            Lesson lesson4 = new Lesson();
            lesson4.setTitle("Basic Greetings and Introductions");
            lesson4.setDescription("Learn how to greet people and introduce yourself in German");
            lesson4.setContent("Basic greetings: Guten Morgen (good morning), Guten Tag (good day), Guten Abend (good evening), Gute Nacht (good night). Introductions: Ich heiße... (my name is), Wie heißen Sie? (what is your name?)");
            lesson4.setLessonNumber(1);
            lesson4.setCourse(germanCourse);
            lessonRepository.save(lesson4);

            Lesson lesson5 = new Lesson();
            lesson5.setTitle("Numbers and Colors");
            lesson5.setDescription("Master German numbers from 1-100 and common colors");
            lesson5.setContent("Numbers: eins (1), zwei (2), drei (3), vier (4), fünf (5), sechs (6), sieben (7), acht (8), neun (9), zehn (10). Colors: rot (red), blau (blue), grün (green), gelb (yellow), schwarz (black), weiß (white).");
            lesson5.setLessonNumber(2);
            lesson5.setCourse(germanCourse);
            lessonRepository.save(lesson5);

            // Create lessons for French course  
            Lesson lesson6 = new Lesson();
            lesson6.setTitle("French in the Office");
            lesson6.setDescription("Vocabulary and expressions used in office environment");
            lesson6.setContent("Basic office vocabulary: le bureau (office), la réunion (meeting), le projet (project), le rapport (report), l'ordinateur (computer), le téléphone (telephone), l'email (email), le collègue (colleague).");
            lesson6.setLessonNumber(1);
            lesson6.setCourse(frenchCourse);
            lessonRepository.save(lesson6);

            
            // Create chats and messages
            Chat englishChat = new Chat();
            englishChat.setChatName("Advanced English Course - James & Sarah");
            englishChat.getPeople().add(james); // student
            englishChat.getPeople().add(sarah); // teacher
            chatRepository.save(englishChat);

            Chat germanChat = new Chat();
            germanChat.setChatName("German for Beginners - Emma & Michael");
            germanChat.getPeople().add(emma); // student/teacher
            germanChat.getPeople().add(michael); // teacher
            chatRepository.save(germanChat);

            Chat frenchChat = new Chat();
            frenchChat.setChatName("Business French - Olivia & Emma");
            frenchChat.getPeople().add(olivia); // student
            frenchChat.getPeople().add(emma); // teacher
            chatRepository.save(frenchChat);

            // Create messages for English chat (James & Sarah)
            Message msg1 = new Message();
            msg1.setText("Hi! I have a question about past tenses in English.");
            msg1.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
            msg1.setChat(englishChat);
            msg1.setSender(james);
            messageRepository.save(msg1);

            Message msg2 = new Message();
            msg2.setText("Hello James! Of course, I'm happy to help. What specifically are you wondering about?");
            msg2.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(1).minusMinutes(30)));
            msg2.setChat(englishChat);
            msg2.setSender(sarah);
            messageRepository.save(msg2);

            Message msg3 = new Message();
            msg3.setText("Is 'I have been working' Present Perfect Continuous or Past Perfect?");
            msg3.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
            msg3.setChat(englishChat);
            msg3.setSender(james);
            messageRepository.save(msg3);

            Message msg4 = new Message();
            msg4.setText("That's Present Perfect Continuous. We use it for actions that started in the past and continue to now. Past Perfect would be 'I had worked'.");
            msg4.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMinutes(30)));
            msg4.setChat(englishChat);
            msg4.setSender(sarah);
            messageRepository.save(msg4);

            // Create messages for German chat (Emma & Michael)
            Message msg5 = new Message();
            msg5.setText("Good day! Could you explain German articles?");
            msg5.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(3)));
            msg5.setChat(germanChat);
            msg5.setSender(emma);
            messageRepository.save(msg5);

            Message msg6 = new Message();
            msg6.setText("Hello Emma! Der (masculine), die (feminine), das (neuter). It's best to learn them with the nouns.");
            msg6.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2).minusMinutes(15)));
            msg6.setChat(germanChat);
            msg6.setSender(michael);
            messageRepository.save(msg6);

            Message msg7 = new Message();
            msg7.setText("How can I remember which noun has which article?");
            msg7.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(2)));
            msg7.setChat(germanChat);
            msg7.setSender(emma);
            messageRepository.save(msg7);

            // Create messages for French chat (Olivia & Emma)
            Message msg8 = new Message();
            msg8.setText("Bonjour! I'm having difficulties with past tenses in French.");
            msg8.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusHours(1)));
            msg8.setChat(frenchChat);
            msg8.setSender(olivia);
            messageRepository.save(msg8);

            Message msg9 = new Message();
            msg9.setText("Bonjour Olivia! Passé composé or imparfait? I can explain the difference.");
            msg9.setTimestamp(Timestamp.valueOf(LocalDateTime.now().minusMinutes(45)));
            msg9.setChat(frenchChat);
            msg9.setSender(emma);
            messageRepository.save(msg9);
        };
    }
}

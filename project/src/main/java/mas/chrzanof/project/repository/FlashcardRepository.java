package mas.chrzanof.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Flashcard;
import mas.chrzanof.project.model.enums.State;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {
    List<Flashcard> findByFlashcardDeckId(Long deckId);
    List<Flashcard> findByState(State state);
} 
package mas.chrzanof.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mas.chrzanof.project.model.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
} 
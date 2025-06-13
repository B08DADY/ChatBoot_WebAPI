package NaviTalk.example.ChatBoot.Repositories;

import NaviTalk.example.ChatBoot.Models.ChatSession;
import NaviTalk.example.ChatBoot.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatSessionRepository extends JpaRepository<ChatSession, Long> {
    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
}
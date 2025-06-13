package NaviTalk.example.ChatBoot.Services;

import NaviTalk.example.ChatBoot.DTOs.ChatSessionDto;
import NaviTalk.example.ChatBoot.Exceptions.ResourceNotFoundException;
import NaviTalk.example.ChatBoot.Models.ChatSession;
import NaviTalk.example.ChatBoot.Models.User;
import NaviTalk.example.ChatBoot.Repositories.ChatSessionRepository;
import NaviTalk.example.ChatBoot.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatSessionService {

    @Autowired private ChatSessionRepository sessionRepository;
    @Autowired private UserRepository userRepository;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    }

    public List<ChatSessionDto> getSessionsForUser(String username) {
        User user = getUserByUsername(username);
        return sessionRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(session -> new ChatSessionDto(session.getId(), session.getTitle(), session.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatSessionDto createSession(String username, String title) {
        User user = getUserByUsername(username);
        ChatSession session = new ChatSession();
        session.setUser(user);
        session.setTitle(title);
        session.setCreatedAt(LocalDateTime.now());

        ChatSession savedSession = sessionRepository.save(session);
        return new ChatSessionDto(savedSession.getId(), savedSession.getTitle(), savedSession.getCreatedAt());
    }

    @Transactional
    public void deleteSession(Long sessionId, String username) {
        User user = getUserByUsername(username);
        ChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with ID: " + sessionId));

        if (!session.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User does not have permission to delete this session.");
        }


        sessionRepository.delete(session);
    }
}
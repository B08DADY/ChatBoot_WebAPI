package NaviTalk.example.ChatBoot.Services;

import NaviTalk.example.ChatBoot.DTOs.ChatMessageDto;
import NaviTalk.example.ChatBoot.Exceptions.ResourceNotFoundException;
import NaviTalk.example.ChatBoot.Models.ChatMessage;
import NaviTalk.example.ChatBoot.Models.ChatSession;
import NaviTalk.example.ChatBoot.Models.User;
import NaviTalk.example.ChatBoot.Repositories.ChatMessageRepository;
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
public class ChatMessageService {

    @Autowired private ChatMessageRepository messageRepository;
    @Autowired private ChatSessionRepository sessionRepository;
    @Autowired private UserRepository userRepository;

    private void verifySessionOwnership(Long sessionId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        ChatSession session = sessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not found"));
        if (!session.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("User cannot access this session.");
        }
    }

    public List<ChatMessageDto> getMessages(Long sessionId, String username) {
        verifySessionOwnership(sessionId, username); // Security check
        return messageRepository.findBySessionIdOrderByTimestampAsc(sessionId).stream()
                .map(msg -> new ChatMessageDto(msg.getId(), msg.getRole(), msg.getContent(), msg.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatMessageDto processNewMessage(Long sessionId, String username, String userContent) {
        verifySessionOwnership(sessionId, username); // Security check
        ChatSession session = sessionRepository.findById(sessionId).orElseThrow(() -> new ResourceNotFoundException("Session not found"));

        // 1. Save the user's message
        ChatMessage userMessage = new ChatMessage();
        userMessage.setSession(session);
        userMessage.setRole("USER");
        userMessage.setContent(userContent);
        userMessage.setTimestamp(LocalDateTime.now());
        messageRepository.save(userMessage);

        // 2. Generate the bot's response
        // =========================================================================
        // === TODO: THIS IS WHERE YOU INTEGRATE YOUR ACTUAL CHATBOT LOGIC/API ===
        String botContent = "This is a dummy response to: '" + userContent + "'";
        // =========================================================================

        // 3. Save the bot's message
        ChatMessage botMessage = new ChatMessage();
        botMessage.setSession(session);
        botMessage.setRole("ASSISTANT");
        botMessage.setContent("""
                Ana Gimini junior""");
        botMessage.setTimestamp(LocalDateTime.now());
        ChatMessage savedBotMessage = messageRepository.save(botMessage);

         //4. Return the bot's response to the client
        return new ChatMessageDto(savedBotMessage.getId(), savedBotMessage.getRole(), savedBotMessage.getContent(), savedBotMessage.getTimestamp());
    }
}
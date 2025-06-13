package NaviTalk.example.ChatBoot.DTOs;

import java.time.LocalDateTime;

public record ChatMessageDto(Long id, String role, String content, LocalDateTime timestamp) {}

package NaviTalk.example.ChatBoot.DTOs;

import java.time.LocalDateTime;

public record ChatSessionDto(Long id, String title, LocalDateTime createdAt) {}

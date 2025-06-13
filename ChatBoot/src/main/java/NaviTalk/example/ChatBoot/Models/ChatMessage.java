package NaviTalk.example.ChatBoot.Models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chat_messages")
public class ChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private ChatSession session;

    @Lob // For potentially long text
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String role; //  "USER" or "ASSISTANT"

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
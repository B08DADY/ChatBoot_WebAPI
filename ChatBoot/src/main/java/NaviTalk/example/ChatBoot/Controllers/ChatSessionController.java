package NaviTalk.example.ChatBoot.Controllers;

import NaviTalk.example.ChatBoot.DTOs.ChatSessionDto;
import NaviTalk.example.ChatBoot.DTOs.CreateSessionDto;
import NaviTalk.example.ChatBoot.Services.ChatSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class ChatSessionController {

    private final ChatSessionService sessionService;

    @Autowired
    public ChatSessionController(ChatSessionService sessionService) {
        this.sessionService = sessionService;
    }


    @GetMapping
    public ResponseEntity<List<ChatSessionDto>> getUserSessions(Authentication authentication) {
        // authentication.getName() safely gets the username from the validated JWT.
        String username = authentication.getName();
        List<ChatSessionDto> sessions = sessionService.getSessionsForUser(username);
        return ResponseEntity.ok(sessions);
    }


    @PostMapping
    public ResponseEntity<ChatSessionDto> createSession(Authentication authentication, @RequestBody(required = false) CreateSessionDto createDto) {
        String username = authentication.getName();
        String title = (createDto != null && createDto.title() != null) ? createDto.title() : "New Chat";

        ChatSessionDto newSession = sessionService.createSession(username, title);
        return new ResponseEntity<>(newSession, HttpStatus.CREATED);
    }


    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Void> deleteSession(@PathVariable Long sessionId, Authentication authentication) {
        String username = authentication.getName();
        sessionService.deleteSession(sessionId, username);
        return ResponseEntity.noContent().build();
    }
}
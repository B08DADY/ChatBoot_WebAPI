package NaviTalk.example.ChatBoot.Controllers;

import NaviTalk.example.ChatBoot.DTOs.ChatMessageDto;
import NaviTalk.example.ChatBoot.DTOs.MessageRequestDto;
import NaviTalk.example.ChatBoot.Services.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sessions/{sessionId}/messages")
public class ChatMessageController {

    private final ChatMessageService messageService;

    @Autowired
    public ChatMessageController(ChatMessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping
    public ResponseEntity<List<ChatMessageDto>> getMessagesForSession(@PathVariable Long sessionId, Authentication authentication) {
        String username = authentication.getName();
        List<ChatMessageDto> messages = messageService.getMessages(sessionId, username);
        return ResponseEntity.ok(messages);
    }


    @PostMapping
    public ResponseEntity<ChatMessageDto> postMessage(
            @PathVariable Long sessionId,
            @RequestBody MessageRequestDto messageRequest,
            Authentication authentication) {

        String username = authentication.getName();
        String userContent = messageRequest.content();

        ChatMessageDto botResponse = messageService.processNewMessage(sessionId, username, userContent);

        return ResponseEntity.ok(botResponse);
    }
}
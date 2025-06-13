package NaviTalk.example.ChatBoot.Controllers;

import NaviTalk.example.ChatBoot.DTOs.AuthResponseDto;
import NaviTalk.example.ChatBoot.DTOs.LoginDto;
import NaviTalk.example.ChatBoot.DTOs.RegisterDto;
import NaviTalk.example.ChatBoot.Services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody RegisterDto registerDto) {
        authService.registerUser(registerDto);
        // Using a Map for a more standard JSON response
        Map<String, String> response = Map.of("message", "User registered successfully!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
        System.out.printf(loginDto.toString() + "\n");
        String token = authService.loginUser(loginDto);
        AuthResponseDto authResponse = new AuthResponseDto(token, loginDto.username());
        return ResponseEntity.ok(authResponse);
    }
}
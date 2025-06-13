package NaviTalk.example.ChatBoot.Services;
import NaviTalk.example.ChatBoot.DTOs.LoginDto;
import NaviTalk.example.ChatBoot.DTOs.RegisterDto;
import NaviTalk.example.ChatBoot.Exceptions.AuthException;
import NaviTalk.example.ChatBoot.Models.User;
import NaviTalk.example.ChatBoot.Repositories.UserRepository;
import NaviTalk.example.ChatBoot.Security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;

    public void registerUser(RegisterDto registerDto) {
        if (userRepository.existsByUsername(registerDto.username())) {
            throw new AuthException("Username is already taken!");
        }
        if (userRepository.existsByEmail(registerDto.email())) {
            throw new AuthException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(registerDto.username());
        user.setEmail(registerDto.email());
        user.setPassword(passwordEncoder.encode(registerDto.password()));

        userRepository.save(user);
    }

    public String loginUser(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.username(), loginDto.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtUtil.generateToken(authentication);
    }
}
package NaviTalk.example.ChatBoot.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST) // Default to 400, but can be overridden in the handler
public class AuthException extends RuntimeException {

    public AuthException(String message) {
        super(message);
    }
}
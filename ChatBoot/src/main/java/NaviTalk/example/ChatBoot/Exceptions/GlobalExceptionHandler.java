package NaviTalk.example.ChatBoot.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class GlobalExceptionHandler {

    // Helper method to create a consistent error response body
    private Map<String, Object> createErrorBody(HttpStatus status, String message, WebRequest request) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        return body;
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        Map<String, Object> body = createErrorBody(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Object> handleAuthException(AuthException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 for things like "Username taken"
        Map<String, Object> body = createErrorBody(status, ex.getMessage(), request);
        return new ResponseEntity<>(body, status);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN; // 403 Forbidden is the correct status here
        Map<String, Object> body = createErrorBody(status, "You do not have permission to access this resource.", request);
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED; // 401
        // For security, use a generic message instead of ex.getMessage()
        Map<String, Object> body = createErrorBody(status, "Invalid credentials.", request);
        return new ResponseEntity<>(body, status);
    }




    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(Exception ex, WebRequest request) {
        // It's good practice to log the full exception for debugging
        ex.printStackTrace(); // Or use a proper logger

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        Map<String, Object> body = createErrorBody(status, "An unexpected internal server error occurred.", request);
        return new ResponseEntity<>(body, status);
    }
}
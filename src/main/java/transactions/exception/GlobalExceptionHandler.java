package transactions.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ParentTransactionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleParentNotFound(ParentTransactionNotFoundException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}
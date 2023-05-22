package pl.diminuen.propertysalessystem.exceptions.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.diminuen.propertysalessystem.exceptions.RefreshTokenException;
import pl.diminuen.propertysalessystem.exceptions.RegistrationException;

@ControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(value = {RegistrationException.class})
    public ResponseEntity<?> handleRegisterException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(value = {RefreshTokenException.class})
    public ResponseEntity<?> handleRefreshTokenException(RefreshTokenException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
}

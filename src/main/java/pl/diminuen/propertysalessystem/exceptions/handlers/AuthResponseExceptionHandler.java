package pl.diminuen.propertysalessystem.exceptions.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.diminuen.propertysalessystem.exceptions.RegistrationException;

@ControllerAdvice
public class AuthResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {RegistrationException.class})
    public ResponseEntity<?> handleRegisterException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

package pl.diminuen.propertysalessystem.exceptions.handlers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.diminuen.propertysalessystem.exceptions.OfferDeletionException;

public class OfferExceptionHandler {
    @ExceptionHandler(value = {OfferDeletionException.class})
    public ResponseEntity<?> handleOfferDeletionException(OfferDeletionException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}

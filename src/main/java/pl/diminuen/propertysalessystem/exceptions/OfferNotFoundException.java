package pl.diminuen.propertysalessystem.exceptions;

public class OfferNotFoundException extends RuntimeException{
    public OfferNotFoundException(String message) {
        super(message);
    }
}

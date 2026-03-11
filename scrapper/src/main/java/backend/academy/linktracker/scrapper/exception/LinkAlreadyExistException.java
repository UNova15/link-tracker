package backend.academy.linktracker.scrapper.exception;

public class LinkAlreadyExistException extends RuntimeException {
    public LinkAlreadyExistException(String message) {
        super(message);
    }
}

package backend.academy.linktracker.scrapper.exception;

public class IncorrectLinkFormatException extends RuntimeException {
    public IncorrectLinkFormatException(String message) {
        super(message);
    }
}

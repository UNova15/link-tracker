package backend.academy.linktracker.scrapper.exception;

public class ChatNotFoundException extends ResourceNotFoundException {
    public ChatNotFoundException(String message) {
        super(message);
    }
}

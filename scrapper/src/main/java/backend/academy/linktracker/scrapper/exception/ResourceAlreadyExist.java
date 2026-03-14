package backend.academy.linktracker.scrapper.exception;

public class ResourceAlreadyExist extends RuntimeException {
    public ResourceAlreadyExist(String message) {
        super(message);
    }
}

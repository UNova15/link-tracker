package backend.academy.linktracker.scrapper.exception;

public class LinkAlreadyExistException extends ResourceAlreadyExist {
    public LinkAlreadyExistException(String message) {
        super(message);
    }
}

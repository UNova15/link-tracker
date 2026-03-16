package backend.academy.linktracker.scrapper.exception;

public class LinkAlreadyExistException extends ResourceAlreadyExist {
    public LinkAlreadyExistException(String url, long chatId) {
        super(String.format("Link with url: %s with user: %d already exist", url, chatId));
    }
}

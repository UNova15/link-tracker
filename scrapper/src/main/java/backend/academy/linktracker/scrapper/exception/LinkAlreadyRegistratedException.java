package backend.academy.linktracker.scrapper.exception;

public class LinkAlreadyRegistratedException extends ResourceAlreadyExist {
    public LinkAlreadyRegistratedException(String url, long chatId) {
        super(String.format("Link with url: %s with user: %d already exist", url, chatId));
    }
}

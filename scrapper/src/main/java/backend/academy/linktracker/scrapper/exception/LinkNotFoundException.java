package backend.academy.linktracker.scrapper.exception;

public class LinkNotFoundException extends ResourceNotFoundException {
    public LinkNotFoundException(String url) {
        super(String.format("Link with url: %s not found", url));
    }

    public LinkNotFoundException(String url, long chatId) {
        super(String.format("Link with user: %d with url: %s not found", chatId, url));
    }
}

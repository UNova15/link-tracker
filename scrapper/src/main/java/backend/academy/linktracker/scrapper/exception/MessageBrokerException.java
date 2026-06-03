package backend.academy.linktracker.scrapper.exception;

public class MessageBrokerException extends RuntimeException {
    public MessageBrokerException(Throwable exception) {
        super(exception);
    }
}

package backend.academy.linktracker.scrapper.exception;

public class SubscriptionNotFoundException extends ResourceNotFoundException {
    public SubscriptionNotFoundException(long chatId, long linkId) {
        super("Subscription with id %d for user %d not found".formatted(chatId, linkId));
    }
}

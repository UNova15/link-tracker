package backend.academy.linktracker.scrapper.exception;

public class ChatNotFoundException extends ResourceNotFoundException {
    public ChatNotFoundException(long chatId) {
        super(String.format("Chat with id %d does not exist", chatId));
    }
}

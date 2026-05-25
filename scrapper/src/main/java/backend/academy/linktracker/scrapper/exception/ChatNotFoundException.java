package backend.academy.linktracker.scrapper.exception;

public class ChatNotFoundException extends ResourceNotFoundException {
    public ChatNotFoundException(long chatId) {
        super(String.format("Chat with linkId %d does not exist", chatId));
    }
}

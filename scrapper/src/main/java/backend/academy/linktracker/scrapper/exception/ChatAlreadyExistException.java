package backend.academy.linktracker.scrapper.exception;

public class ChatAlreadyExistException extends ResourceAlreadyExist {
    public ChatAlreadyExistException(long chatId) {
        super(String.format("Chat with id %d already exist", chatId));
    }
}

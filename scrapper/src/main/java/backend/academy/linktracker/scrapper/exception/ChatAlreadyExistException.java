package backend.academy.linktracker.scrapper.exception;

public class ChatAlreadyExistException extends ResourceAlreadyExist {
    public ChatAlreadyExistException(String message){
        super(message);
    }
}

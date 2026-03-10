package backend.academy.linktracker.scrapper.exception.handler;


import backend.academy.linktracker.scrapper.exception.ApiErrorResponse;
import backend.academy.linktracker.scrapper.exception.ChatAlreadyExistException;
import backend.academy.linktracker.scrapper.exception.ChatNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChatAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handleChatAlreadyExistException(ChatAlreadyExistException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                "Чат уже существует",
                "409",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace())
            );
    }

    @ExceptionHandler(ChatNotExistException.class)
    public ResponseEntity<ApiErrorResponse> handleChatNotExistException(ChatNotExistException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                "Чат не существует",
                "404",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace()
            ));
    }
}

package backend.academy.linktracker.scrapper.exception.handler;


import backend.academy.linktracker.scrapper.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ChatAlreadyExistException.class, LinkAlreadyExistException.class})
    public ResponseEntity<ApiErrorResponse> handleChatAlreadyExistException(ChatAlreadyExistException exception) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(new ApiErrorResponse(
                "Ресурс уже существует",
                "409",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace())
            );
    }

    @ExceptionHandler({ChatNotFoundException.class, LinkNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleChatNotExistException(ChatNotFoundException exception) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ApiErrorResponse(
                "Ресурс не существует",
                "404",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace()
            ));
    }

    @Override
    public ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException exception,
                                                            HttpHeaders headers, HttpStatusCode status,
                                                            WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                "Некорректные параметры запроса",
                "400",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace()
            ));
    }

    @ExceptionHandler(IncorrectLinkFormatException.class)
    public ResponseEntity<ApiErrorResponse> handleIncorrectLinkFormatException(IncorrectLinkFormatException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                "Некорректная ссылка на ресурс",
                "400",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getStackTrace()
            ));
    }
}

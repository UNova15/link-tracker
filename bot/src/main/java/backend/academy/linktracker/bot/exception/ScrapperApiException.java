package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.dto.ApiErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
public class ScrapperApiException extends RuntimeException {
    private final ApiErrorResponse errorResponse;
    private final HttpStatusCode code;
}

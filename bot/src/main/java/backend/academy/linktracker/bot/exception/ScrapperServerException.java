package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ScrapperServerException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ScrapperServerException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}

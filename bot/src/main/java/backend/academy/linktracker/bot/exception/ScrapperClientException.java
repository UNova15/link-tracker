package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;
import lombok.Getter;

@Getter
public class ScrapperClientException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ScrapperClientException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }
}

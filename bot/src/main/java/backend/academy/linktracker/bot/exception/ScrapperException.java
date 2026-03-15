package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;

public class ScrapperException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ScrapperException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getDescription(){
        return errorResponse.description();
    }
}

package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;

public class ScrapperServerException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ScrapperServerException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getDescription(){
        return errorResponse.description();
    }

    public String getStack(){
        return String.join(" ",errorResponse.stackTrace());
    }
}

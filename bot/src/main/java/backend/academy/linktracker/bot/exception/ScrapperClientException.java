package backend.academy.linktracker.bot.exception;

import backend.academy.linktracker.bot.model.ApiErrorResponse;

public class ScrapperClientException extends RuntimeException {
    private final ApiErrorResponse errorResponse;

    public ScrapperClientException(ApiErrorResponse errorResponse) {
        this.errorResponse = errorResponse;
    }

    public String getDescription(){
        return errorResponse.description();
    }

    public String getStack(){
        return String.join(" ",errorResponse.stackTrace());
    }
}

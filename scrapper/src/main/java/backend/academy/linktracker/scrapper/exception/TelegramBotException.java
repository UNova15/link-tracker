package backend.academy.linktracker.scrapper.exception;

import lombok.Getter;

@Getter
public class TelegramBotException extends RuntimeException {
    private final ApiErrorResponse apiErrorResponse;

    public TelegramBotException(ApiErrorResponse apiErrorResponse) {
        this.apiErrorResponse = apiErrorResponse;
    }
}

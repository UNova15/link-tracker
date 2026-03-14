package backend.academy.linktracker.bot.exception;

import java.util.Arrays;

public record ApiErrorResponse(String description, String code, String exceptionName, String exceptionMessage,
                               String[] stackTrace) {
    public ApiErrorResponse(String description, String code, String exceptionName, String exceptionMessage,
                            StackTraceElement[] stackTrace) {
        String[] formatedStackTrace = Arrays.stream(stackTrace).map(StackTraceElement::toString).toArray(String[]::new);
        this(description, code, exceptionName, exceptionMessage, formatedStackTrace);
    }
}

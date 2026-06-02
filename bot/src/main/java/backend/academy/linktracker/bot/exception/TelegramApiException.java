package backend.academy.linktracker.bot.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TelegramApiException extends RuntimeException {
    private final int errorCode;
    private final String description;
}

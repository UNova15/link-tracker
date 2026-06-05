package backend.academy.linktracker.bot.client;

import backend.academy.linktracker.bot.exception.TelegramApiException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TelegramService {
    private final TelegramBot bot;

    @Retry(name = "telegram")
    @CircuitBreaker(name = "telegram")
    public void sendNotification(long id, String description) {
        SendMessage message = new SendMessage(id, description);
        execute(message);
    }

    @Retry(name = "telegram")
    @CircuitBreaker(name = "telegram")
    public void sendResponse(long id, String response) {
        SendMessage message = new SendMessage(id, response);
        execute(message);
    }

    private void execute(SendMessage message) {
        SendResponse response = bot.execute(message);

        if (!response.isOk()) {
            throw new TelegramApiException(response.errorCode(), response.description());
        }
    }
}

package backend.academy.linktracker.bot.client;

import static backend.academy.linktracker.bot.configuration.TelegramBotConfiguration.TELEGRAM_CONFIG_NAME;

import backend.academy.linktracker.bot.exception.TelegramApiException;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Retry(name = TELEGRAM_CONFIG_NAME)
@CircuitBreaker(name = TELEGRAM_CONFIG_NAME)
@AllArgsConstructor
public class BotClient {
    private final TelegramBot bot;

    public void sendMessage(long id, String description) {
        SendMessage message = new SendMessage(id, description);

        SendResponse response = bot.execute(message);
        if (!response.isOk()) {
            throw new TelegramApiException(response.errorCode(), response.description());
        }
    }
}

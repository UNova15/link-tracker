package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UnknownUserHandler implements Handler {
    private static final String MESSAGE = "Неизвестная команда. Чтобы начать диалог выполните команду /start";

    @Override
    public String handle(UserMessage message, SessionData session) {
        try (MDC.MDCCloseable ignored = MDC.putCloseable("userId", String.valueOf(message.id()))) {
            MDC.put("userMessage", message.text());
            log.info("Неизвестный пользователь");
        }

        return MESSAGE;
    }
}

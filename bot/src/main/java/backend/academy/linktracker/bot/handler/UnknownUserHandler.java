package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class UnknownUserHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(UnknownUserHandler.class);

    @Override
    public String handle(UserMessage message, SessionData session) {
        try (MDC.MDCCloseable ignored = MDC.putCloseable(
            "userId", String.valueOf(message.id()))) {
            MDC.put("userMessage", message.text());
            logger.info("Неизвестный пользователь");
        }

        return "Неизвестная команда. Чтобы начать диалог выполните команду /start";
    }

}

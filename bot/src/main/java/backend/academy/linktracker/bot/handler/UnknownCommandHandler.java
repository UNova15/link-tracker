package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class UnknownCommandHandler implements Handler {
    private static final Logger logger = LoggerFactory.getLogger(UnknownCommandHandler.class);

    @Override
    public String handle(UserMessage message, SessionData session) {
        try (MDC.MDCCloseable ignored = MDC.putCloseable("userId", String.valueOf(message.id()))) {
            MDC.put("userMessage", message.text());
            logger.info("Некорректное сообщение");
        }

        return "Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.";
    }
}

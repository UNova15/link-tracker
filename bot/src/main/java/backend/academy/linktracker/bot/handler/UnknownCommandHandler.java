package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UnknownCommandHandler implements Handler {
    private static final String MESSAGE = "Неизвестная команда. Воспользуйтесь /help, чтобы посмотреть список доступных команд.";

    @Override
    public String handle(UserMessage message, SessionData session) {
        try (MDC.MDCCloseable ignored = MDC.putCloseable("userId", String.valueOf(message.id()))) {
            MDC.put("userMessage", message.text());
            log.info("Некорректное сообщение");
        }

        return MESSAGE;
    }
}

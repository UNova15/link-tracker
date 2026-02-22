package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.configuration.CommandRegistry;
import backend.academy.linktracker.bot.service.commands.Handler;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotUpdateListener implements UpdatesListener {
    private static final Logger logger = LoggerFactory.getLogger(BotUpdateListener.class);

    private final TelegramBot bot;
    private final CommandRegistry commandRegistry;

    @Autowired
    public BotUpdateListener(TelegramBot bot, CommandRegistry commandRegistry) {
        this.bot = bot;
        this.commandRegistry = commandRegistry;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null) {
                Handler handler = commandRegistry
                        .getCommandHandler(update.message().text())
                        .orElse(commandRegistry.getUnknownCommandHandler());

                if (handler == commandRegistry.getUnknownCommandHandler()) {
                    try (MDC.MDCCloseable ignored = MDC.putCloseable(
                            "userId", String.valueOf(update.message().chat().id()))) {
                        MDC.put("userMessage", update.message().text());
                        logger.info("Некорректное сообщение");
                    }
                }

                bot.execute(handler.handle(update));
            }
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

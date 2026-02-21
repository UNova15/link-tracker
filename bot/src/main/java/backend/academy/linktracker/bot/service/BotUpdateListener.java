package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.service.commands.CommandHandler;
import backend.academy.linktracker.bot.service.commands.UnknownCommandHandler;
import backend.academy.linktracker.bot.validator.MessageValidator;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BotUpdateListener implements UpdatesListener {
    private static final Logger logger = LoggerFactory.getLogger(BotUpdateListener.class);

    private List<CommandHandler> handlers;
    private MessageValidator validator;
    private UnknownCommandHandler unknownHandler;
    private TelegramBot bot;


    @Autowired
    public BotUpdateListener(TelegramBot bot, List<CommandHandler> handlers,
                             UnknownCommandHandler unknownHandler, MessageValidator validator) {
        this.bot = bot;
        this.validator = validator;
        this.handlers = handlers;
        this.unknownHandler = unknownHandler;
    }


    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (!validator.isValidMessage(update.message(), handlers)) {

                try (MDC.MDCCloseable ignored = MDC.putCloseable("userId", String.valueOf(update.message().chat().id()))) {
                    MDC.put("userMessage", update.message().text());
                    logger.info("Некорректное сообщение");

                    SendMessage responseMessage = unknownHandler.handle(update);
                    bot.execute(responseMessage);
                }

            } else {
                String text = update.message().text();
                CommandHandler handler = handlers.stream()
                    .filter(command -> command.getName().equals(text))
                    .findFirst()
                    .get();

                SendMessage responseMessage = handler.handle(update);
                bot.execute(responseMessage);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

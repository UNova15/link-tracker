package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.service.commands.CommandService;
import backend.academy.linktracker.bot.validator.MessageValidator;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class BotUpdateListener implements UpdatesListener {
    private List<CommandService> handlers;
    private MessageValidator validator;
    private UnknownCommandService unknownHandler;
    private TelegramBot bot;


    @Autowired
    public BotUpdateListener(TelegramBot bot, List<CommandService> handlers,
                             UnknownCommandService unknownHandler, MessageValidator validator) {
        this.bot = bot;
        this.validator = validator;
        this.handlers = handlers;
        this.unknownHandler = unknownHandler;
    }


    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (!validator.isValidMessage(update.message(), handlers)) {
                SendMessage responseMessage = unknownHandler.handle(update);
                bot.execute(responseMessage);
            } else {
                String text = update.message().text();
                CommandService handler = handlers.stream()
                    .filter(command -> command.getCommandName().equals(text))
                    .findFirst()
                    .get();

                SendMessage responseMessage = handler.handle(update);
                bot.execute(responseMessage);
            }
        }

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }
}

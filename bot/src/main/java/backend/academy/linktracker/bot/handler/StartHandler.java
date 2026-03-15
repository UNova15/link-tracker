package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.scrapper.ScrapperChatClient;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import backend.academy.linktracker.bot.handler.clientstatehandlers.AwaitCommandState;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartHandler extends CommandHandler {
    private final AwaitCommandState awaitCommand;
    private final SessionStorage storage;
    private final ScrapperChatClient scrapperChatClient;

    @Autowired
    public StartHandler(ScrapperChatClient scrapperChatClient, SessionStorage storage, AwaitCommandState awaitCommand) {
        super(new BotCommand("/start", "Запуск бота"));
        this.scrapperChatClient = scrapperChatClient;
        this.storage = storage;
        this.awaitCommand = awaitCommand;
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        scrapperChatClient.registrationChat(id);
        //создание сессии
        storage.updateSession(id, awaitCommand);
        return new SendMessage(id, "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.");
    }
}

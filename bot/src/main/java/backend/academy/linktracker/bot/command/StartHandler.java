package backend.academy.linktracker.bot.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperChatClient;
import backend.academy.linktracker.bot.client.telegram.SessionStorage;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartHandler extends CommandHandler {
    private final SessionStorage storage;
    private final ScrapperChatClient scrapperChatClient;

    @Autowired
    public StartHandler(ScrapperChatClient scrapperChatClient,SessionStorage storage) {
        super(new BotCommand("/start", "Запуск бота"));
        this.scrapperChatClient = scrapperChatClient;
        this.storage = storage;
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        scrapperChatClient.registrationChat(id);
        storage.saveSession(id);
        return new SendMessage(id, "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.");
    }
}

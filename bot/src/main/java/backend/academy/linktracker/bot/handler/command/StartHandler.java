package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperChatClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.model.UserMessage;
import com.pengrad.telegrambot.model.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartHandler extends CommandHandler {
    private final AwaitCommandState awaitCommand;
    private final ScrapperChatClient scrapperChatClient;

    @Autowired
    public StartHandler(ScrapperChatClient scrapperChatClient, AwaitCommandState awaitCommand) {
        super(new BotCommand("/start", "Запуск бота"));
        this.scrapperChatClient = scrapperChatClient;
        this.awaitCommand = awaitCommand;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        scrapperChatClient.registrationChat(message.id());
        session.setState(awaitCommand);
        return "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.";
    }
}

package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperChatClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.exception.ScrapperException;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.model.UserMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class StartHandler extends CommandHandler {
    private final Logger logger = LoggerFactory.getLogger(StartHandler.class);
    private final ScrapperChatClient scrapperChatClient;

    @Autowired
    public StartHandler(ScrapperChatClient scrapperChatClient, @Lazy AwaitCommandState awaitCommand) {
        super(new Command("/start", "Запуск бота"), awaitCommand);
        this.scrapperChatClient = scrapperChatClient;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            scrapperChatClient.registrationChat(message.id());
            changeState(session);
            return "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.";
        } catch (ScrapperException exception) {
            logger.warn("Ошибка сохранения пользователя {}", message.id(), exception);
            return "Ошибка сохранения пользователя. Попробуйте позже";
        }
    }
}

package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperChatClient;
import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class StartHandler extends CommandHandler {
    private static final String SUCCESS_MESSAGE =
            "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.";
    private static final String ERROR_MESSAGE = "Ошибка сохранения пользователя. Попробуйте позже";

    private final ScrapperChatClient scrapperChatClient;

    public StartHandler(ScrapperChatClient scrapperChatClient, @Lazy AwaitCommandState awaitCommand) {
        super(new Command("/start", "Запуск бота"), awaitCommand);
        this.scrapperChatClient = scrapperChatClient;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            scrapperChatClient.registrationChat(message.id());
            changeState(session);
            return SUCCESS_MESSAGE;
        } catch (ScrapperClientException exception) {
            log.error(
                    "Ошибка сохранения пользователя {}. {}",
                    message.id(),
                    exception.getErrorResponse().stackTrace());
            return ERROR_MESSAGE;
        }
    }
}

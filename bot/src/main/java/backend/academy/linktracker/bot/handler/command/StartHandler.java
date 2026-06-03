package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.ScrapperService;
import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
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

    private final ScrapperService scrapper;

    public StartHandler(ScrapperService scrapper, @Lazy AwaitCommandState awaitCommand) {
        super(new Command("/start", "Запуск бота"), awaitCommand);
        this.scrapper = scrapper;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            scrapper.registrationChat(message.id());
            changeState(session);
            return SUCCESS_MESSAGE;
        } catch (Exception exception) {
            log.error("Ошибка сохранения пользователя {}. {}", message.id(), exception.getMessage());
            return ERROR_MESSAGE;
        }
    }
}

package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.ScrapperService;
import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.ListCommandHelper;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ListHandler extends CommandHandler {
    private static final String ERROR_MESSAGE = "Ошибка при поиске ссылок. Повторите попытке позже";

    private final ScrapperService scrapper;
    private final ListCommandHelper listCommandHelper;
    private final RequestArgsParser parser;

    public ListHandler(
            @Lazy AwaitCommandState state,
            ScrapperService scrapper,
            RequestArgsParser parser,
            ListCommandHelper listCommandHelper) {
        super(new Command("/list", "Вывод списка всех отслеживаемых ссылок"), state);
        this.scrapper = scrapper;
        this.listCommandHelper = listCommandHelper;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            Optional<String> tag = parser.parseFirstCommandArgument(message.text());

            ListLinkResponse response = scrapper.getLinks(message.id());

            List<String> links = listCommandHelper.filterLinksByTag(response, tag);

            changeState(session);
            return listCommandHelper.formateResponse(links);
        } catch (Exception exception) {
            log.error("Ошибка при поиске ссылок пользователя {}. {}", message.id(), exception.getMessage());
            changeState(session);
            return ERROR_MESSAGE;
        }
    }
}

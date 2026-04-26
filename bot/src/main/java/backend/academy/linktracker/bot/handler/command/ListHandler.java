package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.dto.LinkResponse;
import backend.academy.linktracker.bot.dto.ListLinkResponse;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

// Возможно нарушает srp но не знаю как исправить
@Component
@Slf4j
public class ListHandler extends CommandHandler {
    private static final String ERROR_MESSAGE = "Ошибка при поиске ссылок. Повторите попытке позже";

    private final ScrapperLinkClient scrapperLinkClient;
    private final RequestArgsParser parser;

    public ListHandler(@Lazy AwaitCommandState state, ScrapperLinkClient scrapperLinkClient, RequestArgsParser parser) {
        super(new Command("/list", "Вывод списка всех отслеживаемых ссылок"), state);
        this.scrapperLinkClient = scrapperLinkClient;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            Optional<String> tag = parser.parseListTag(message.text());
            //фильтрация тегов должна быть на стороне скраппера, а потом уже в бд
            ListLinkResponse response = scrapperLinkClient.getLinks(message.id());

            List<String> links = filterLinksByTag(response, tag);

            changeState(session);
            return formateResponse(links);
        } catch (ScrapperClientException exception) {
            log.error(
                    "Ошибка при поиске ссылок пользователя {}. {}",
                    message.id(),
                    exception.getErrorResponse().stackTrace());
            return ERROR_MESSAGE;
        }
    }

    private List<String> filterLinksByTag(ListLinkResponse response, Optional<String> tag) {
        return tag.map(s -> response.links().stream()
                        .filter(link -> link.tags().contains(s))
                        .map(LinkResponse::url)
                        .toList())
                .orElseGet(
                        () -> response.links().stream().map(LinkResponse::url).toList());
    }

    private String formateResponse(List<String> links) {
        StringBuilder builder = new StringBuilder();
        builder.append("Отслеживаемые ссылки:\n");
        for (String link : links) {
            builder.append(link);
            builder.append("\n");
        }
        return builder.toString();
    }
}

package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.model.LinkResponse;
import backend.academy.linktracker.bot.model.ListLinkResponse;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
public class ListHandler extends CommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ListHandler.class);
    private final ScrapperLinkClient scrapperLinkClient;
    private final RequestArgsParser parser;

    @Autowired
    public ListHandler(@Lazy AwaitCommandState state, ScrapperLinkClient scrapperLinkClient, RequestArgsParser parser) {
        super(new Command("/list", "Вывод списка всех отслеживаемых ссылок"), state);
        this.scrapperLinkClient = scrapperLinkClient;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        try {
            Optional<String> tag = parser.parseListTag(message.text());

            ListLinkResponse response = scrapperLinkClient.getLinks(message.id());

            List<String> links = filterLinksByTag(response, tag);

            changeState(session);
            return formateResponse(links);
        } catch (ScrapperClientException exception) {
            logger.warn("Ошибка при поиске ссылок пользователя {}",message.id());
            return "Ошибка при поиске ссылок. Повторите попытке позже";
        }
    }

    private List<String> filterLinksByTag(ListLinkResponse response, Optional<String> tag) {
        if (tag.isEmpty()) {
            return response.links().stream()
                .map(LinkResponse::url)
                .toList();
        }
        return response.links().stream()
            .filter(link -> link.tags().contains(tag.get()))
            .map(LinkResponse::url)
            .toList();
    }

    private String formateResponse(List<String> links) {
        StringBuilder builder = new StringBuilder();

        for (String link : links) {
            builder.append(link);
            builder.append("/n");
        }
        return builder.toString();
    }
}

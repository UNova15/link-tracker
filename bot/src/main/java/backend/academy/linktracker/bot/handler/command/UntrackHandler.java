package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.client.scrapper.ScrapperLinkClient;
import backend.academy.linktracker.bot.exception.ScrapperClientException;
import backend.academy.linktracker.bot.domain.Command;
import backend.academy.linktracker.bot.dto.RemoveLinkRequest;
import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.AwaitCommandState;
import backend.academy.linktracker.bot.util.RequestArgsParser;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UntrackHandler extends CommandHandler {
    private static final String SUCCESS_MESSAGE = "Ссылка успешно удалена";
    private static final String ERROR_MESSAGE = "Ошибка удаления ссылки";
    private static final String MISSING_LINK_TO_RESOURCE = "Отсутствует ссылка на удаляемый ресурс";

    private final ScrapperLinkClient scrapperClient;
    private final RequestArgsParser parser;

    public UntrackHandler(@Lazy AwaitCommandState newState, ScrapperLinkClient client, RequestArgsParser parser) {
        super(new Command("/untrack", "Прекращение отслеживания ссылки"), newState);
        this.scrapperClient = client;
        this.parser = parser;
    }

    @Override
    public String handle(UserMessage message, SessionData session) {
        Optional<String> link = parser.parseRemoveLink(message.text());

        RemoveLinkRequest request = new RemoveLinkRequest(link.orElseGet(() -> {
            log.warn("Отсутствует ссылка на удаляемый ресурс у пользователя {}", message.id());
            return MISSING_LINK_TO_RESOURCE;
        }));

        try {
            scrapperClient.removeLink(message.id(), request);
            changeState(session);
            return SUCCESS_MESSAGE;
        } catch (ScrapperClientException exception) {
            log.error(
                    "Ошибка удаления ссылки {} пользователя {}. {}",
                    message.text(),
                    message.id(),
                    exception.getErrorResponse().stackTrace());
            return ERROR_MESSAGE;
        }
    }
}

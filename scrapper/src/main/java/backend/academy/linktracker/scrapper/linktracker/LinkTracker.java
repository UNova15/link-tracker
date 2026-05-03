package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.tgclient.TelegramBotClient;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkTracker {
    private static final String UPDATE_MESSAGE = "Обновление ссылки";

    private final TelegramBotClient tgClient;
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, LinkChecker> checkers;

    public LinkTracker(
            List<LinkChecker> checkers,
            TelegramBotClient telegramBot,
            LinkRepository linkRepository,
            SubscriptionRepository subscriptionRepository) {
        this.linkRepository = linkRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.checkers = checkers.stream().collect(Collectors.toMap(LinkChecker::getLinkType, Function.identity()));
        this.tgClient = telegramBot;
    }

    @Scheduled(fixedDelayString = "${app.scheduler-interval}")
    public void updateNotification() {

        Collection<Link> activeLink = linkRepository.getAllLinks();
        for (Link link : activeLink) {
            LinkChecker checker = checkers.get(link.type());
            boolean isUpdated = checker.checkLink(link);
            try {
                if (isUpdated) {
                    List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.id());
                    LinkUpdate update = new LinkUpdate(link.id(), link.url(), UPDATE_MESSAGE, chatsId);

                    tgClient.sendUpdateRequest(update);
                }
                linkRepository.updateLink(new Link(link, Instant.now()));
            } catch (TelegramBotException exception) {
                log.error(
                        "Ошибка в уведомлении пользователей об изменения по ссылке: {}. {}",
                        link.url(),
                        exception.getApiErrorResponse().description());
            }
        }
    }
}

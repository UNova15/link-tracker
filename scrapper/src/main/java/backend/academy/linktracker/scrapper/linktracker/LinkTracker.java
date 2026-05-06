package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.tgclient.TelegramBotClient;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LinkTracker {
    private static final String UPDATE_MESSAGE = "Обновление ссылки";
    private static final long LINKS_LIMIT = 100;

    private final TelegramBotClient tgClient;
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, LinkChecker> checkers;

    @Value("${app.scheduler-interval}")
    private long scanTime;

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
        Instant delayTime = Instant.now().minusMillis(scanTime);
        long lastCheckId = 0;

        while (true) {
            List<Link> activeLinks = linkRepository.findLinksToCheck(lastCheckId, LINKS_LIMIT, delayTime);

            if (activeLinks.isEmpty()) {
                break;
            }
            processLinks(activeLinks);

            lastCheckId = activeLinks.getLast().getId();
        }
    }


    private void processLinks(List<Link> activeLink) {
        for (Link link : activeLink) {
            LinkChecker checker = checkers.get(link.getType());

            try {
                boolean isUpdated = checker.checkLink(link);

                if (isUpdated) {
                    List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());
                    LinkUpdate update = new LinkUpdate(link.getId(), link.getUrl(), UPDATE_MESSAGE, chatsId);
                    tgClient.sendUpdateRequest(update);
                }

                link.markCheckedNow();
                linkRepository.update(link);
            } catch (TelegramBotException exception) {
                log.error(
                    "Ошибка в уведомлении пользователей об изменениях по ссылке: {}. {}",
                    link.getUrl(),
                    exception.getApiErrorResponse().description());
            } catch (Exception exception) {
                log.error("Ошибка при проверке ссылки {}: {}", link.getUrl(), exception.getMessage(), exception);
            }
        }
    }
}

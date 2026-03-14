package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.model.linkdto.Link;
import backend.academy.linktracker.scrapper.model.LinkType;
import backend.academy.linktracker.scrapper.model.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import backend.academy.linktracker.scrapper.tgclient.TelegramBotClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class LinkTracker {
    private final TelegramBotClient tgClient;
    private final LinkRepository linkRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, LinkChecker> checkers;

    @Autowired
    public LinkTracker(List<LinkChecker> checkers, TelegramBotClient telegramBot,
                       LinkRepository linkRepository, SubscriptionRepository subscriptionRepository) {
        this.linkRepository = linkRepository;
        this.subscriptionRepository = subscriptionRepository;
        //TODO сложная логика в конструкторе
        this.checkers = checkers.stream()
            .collect(Collectors.toMap(LinkChecker::getLinkType, Function.identity()));
        this.tgClient = telegramBot;
    }

    @Scheduled(fixedDelayString = "${app.shedulerinterval}")
    public void updateNotification() {

        Set<Link> activeLink = linkRepository.getAllLinks();

        for (Link link : activeLink) {
            LinkChecker checker = checkers.get(link.type());
            boolean isUpdated = checker.checkLink(link);

            if (isUpdated) {
                List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.id());
                LinkUpdate update = new LinkUpdate(link.id(), link.url(), "Обновление ссылки", chatsId);

                tgClient.sendUpdateRequest(update);
            }
        }
    }
}

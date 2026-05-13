package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.LinkChecker;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LinkProcessor {
    private static final String UPDATE_MESSAGE = "Обновление ссылки";
    private static final String ERROR_MESSAGE = "Ошибка проверки ссылки: %s";

    private final MessageSender sender;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, LinkChecker> checkers;

    private void sendNotification(Link link, String message) {
        List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());
        LinkUpdate update = new LinkUpdate(link.getId(), link.getUrl(), message, chatsId);
        sender.send(update);
    }

    public void processLinks(List<Link> activeLink) {
        for (Link link : activeLink) {
            LinkChecker checker = checkers.get(link.getType());

            try {
                boolean isUpdated = checker.checkLink(link);

                if (isUpdated) {
                    sendNotification(link, UPDATE_MESSAGE);
                }

                link.markCheckedNow();
            } catch (TelegramBotException exception) {
                log.error(
                        "Ошибка в уведомлении пользователей об изменениях по ссылке: {}. {}",
                        link.getUrl(),
                        exception.getApiErrorResponse().description());
            } catch (Exception exception) {
                log.error("Ошибка при проверке ссылки {}:", link.getUrl(), exception);
                sendNotification(link, ERROR_MESSAGE.formatted(link.getUrl()));
            }
        }
    }
}

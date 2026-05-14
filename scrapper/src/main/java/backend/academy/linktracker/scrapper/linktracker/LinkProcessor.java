package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.dto.linkdto.LinkUpdate;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.ResourceRequester;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LinkProcessor {
    private static final String ERROR_MESSAGE = "Ошибка проверки ссылки: %s";

    private final MessageSender sender;
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, ResourceRequester> checkers;

    private final ExecutorService executorService;
    private final int numberOfThreads;

    public void runProcessLinks(List<Link> activeLink) {
        int total = activeLink.size();
        if (total == 0) return;

        int chunkSize = (int) Math.ceil((double) total / numberOfThreads);

        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < total; i += chunkSize) {
            int end = Math.min(total, i + chunkSize);

            List<Link> chunk = activeLink.subList(i, end);

            futures.add(CompletableFuture.runAsync(
                    () -> {
                        for (Link link : chunk) {
                            processLink(link);
                        }
                    },
                    executorService));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
    }

    private void processLink(Link link) {
        ResourceRequester checker = checkers.get(link.getType());

        try {
            Optional<String> message = checker.check(link);

            message.ifPresent(mes -> sendNotification(link, mes));

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

    private void sendNotification(Link link, String message) {
        List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());
        LinkUpdate update = new LinkUpdate(link.getId(), link.getUrl(), message, chatsId);
        sender.send(update);
    }
}

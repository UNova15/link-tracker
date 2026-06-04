package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.UpdateHandler;
import backend.academy.linktracker.scrapper.mapper.NotificationMapper;
import backend.academy.linktracker.scrapper.repository.SubscriptionRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class LinkProcessor {
    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, UpdateHandler> linkProcessors;
    private final NotificationMapper mapper;

    private final ExecutorService executorService;
    private final int numberOfThreads;

    public List<Notification> runProcessLinks(List<Link> activeLink) {
        int total = activeLink.size();
        if (total == 0) return List.of();

        int chunkSize = (int) Math.ceil((double) total / numberOfThreads);

        List<CompletableFuture<List<Notification>>> futures = new ArrayList<>();
        for (int i = 0; i < total; i += chunkSize) {
            int end = Math.min(total, i + chunkSize);

            List<Link> chunk = activeLink.subList(i, end);

            futures.add(CompletableFuture.supplyAsync(
                    () -> {
                        List<Notification> notifications = new ArrayList<>();
                        for (Link link : chunk) {
                            notifications.addAll(checkLink(link));
                        }
                        return notifications;
                    },
                    executorService));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .toList();
    }

    private List<Notification> checkLink(Link link) {
        UpdateHandler processor = linkProcessors.get(link.getType());

        try {
            Optional<ProcessingResult> result = processor.process(link);

            result.ifPresent(processingResult -> link.updateLastUpdateTime(processingResult.newUpdateTime()));
            link.markCheckedNow();

            List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());

            return result.map(
                            res -> mapper.toNotification(res, UUID.randomUUID(), link.getId(), link.getUrl(), chatsId))
                    .orElse(List.of());

        } catch (Exception exception) {
            log.error("Ошибка при проверке ссылки {}:", link.getUrl(), exception);
            return List.of();
        }
    }
}

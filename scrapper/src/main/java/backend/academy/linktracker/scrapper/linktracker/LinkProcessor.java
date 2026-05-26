package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.linktracker.linkchecker.ResourceHandler;
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
    private static final String ERROR_MESSAGE = "Ошибка проверки ссылки: %s";

    private final SubscriptionRepository subscriptionRepository;
    private final Map<LinkType, ResourceHandler> linkProcessors;

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
                        List<Notification> updates = new ArrayList<>();
                        for (Link link : chunk) {
                            checkLink(link).ifPresent(updates::add);
                        }
                        return updates;
                    },
                    executorService));
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(Collection::stream)
                .toList();
    }

    private Optional<Notification> checkLink(Link link) {
        ResourceHandler processor = linkProcessors.get(link.getType());

        try {
            Optional<ProcessingResult> result = processor.process(link);

            result.ifPresent(processingResult -> link.updateLastUpdateTime(processingResult.newUpdateTime()));
            link.markCheckedNow();

            List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());
            return result.map(
                    res -> Notification.createNew(UUID.randomUUID(), link.getId(), link.getUrl(), res.text(), chatsId));
        } catch (Exception exception) {
            log.error("Ошибка при проверке ссылки {}:", link.getUrl(), exception);

            List<Long> chatsId = subscriptionRepository.findChatsIdByLinkId(link.getId());
            return Optional.of(Notification.createNew(
                    UUID.randomUUID(), link.getId(), link.getUrl(), ERROR_MESSAGE.formatted(link.getUrl()), chatsId));
        }
    }
}

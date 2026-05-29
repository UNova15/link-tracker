package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkTracker {
    private final LinkProcessor processor;
    private final LinkService linkService;

    @Value("${app.age-of-links}")
    private long ageOfLinks;

    @Value("${app.batch-size}")
    private long linksLimit;

    @Scheduled(fixedDelayString = "${app.check-link-interval}")
    public void sendNotification() {
        // Время, позже которого ссылки считаются устаревшими.
        // Если с последнего момента обновления ссылки прошло более scanTime миллисекунд ссылка считается устаревшей
        long lastCheckId = 0;

        while (true) {
            List<Link> activeLinks = linkService.findLinksFilteredByLastCheck(lastCheckId, linksLimit, ageOfLinks);

            if (activeLinks.isEmpty()) {
                break;
            }

            List<Notification> updated = processor.runProcessLinks(activeLinks);

            linkService.saveUpdatedLinksAndOutboxRecord(activeLinks, updated);
            lastCheckId = activeLinks.getLast().getId();
        }
    }
}

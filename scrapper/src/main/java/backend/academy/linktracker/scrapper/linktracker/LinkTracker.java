package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.properties.ScrapperProperties;
import backend.academy.linktracker.scrapper.service.LinkService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkTracker {
    private final LinkProcessor processor;
    private final LinkService linkService;
    private final ScrapperProperties properties;

    @Scheduled(fixedDelayString = "${app.scrapper-settings.check-link-interval}")
    public void sendNotification() {
        long lastCheckId = 0;

        while (true) {
            List<Link> activeLinks = linkService.findLinksFilteredByLastCheck(
                    lastCheckId, properties.batchSize(), properties.ageOfLinks());

            if (activeLinks.isEmpty()) {
                break;
            }

            List<Notification> updated = processor.runProcessLinks(activeLinks);

            linkService.saveUpdatedLinksAndOutboxRecord(activeLinks, updated);
            lastCheckId = activeLinks.getLast().getId();
        }
    }
}

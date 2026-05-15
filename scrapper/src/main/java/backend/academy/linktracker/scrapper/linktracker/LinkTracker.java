package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.repository.LinkRepository;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LinkTracker {
    private final LinkProcessor processor;
    private final LinkRepository linkRepository;

    @Value("${app.age-of-links}")
    private long scanTime;

    @Value("${app.batch-size}")
    private long batchSize;

    @Scheduled(fixedDelayString = "${app.scheduler-interval}")
    @Transactional
    public void sendNotification() {
        // Время, позже которого ссылки считаются устаревшими.
        // Если с последнего момента обновления ссылки прошло более scanTime миллисекунд ссылка считается устаревшей
        Instant delayTime = Instant.now().minusMillis(scanTime);
        long lastCheckId = 0;

        while (true) {
            List<Link> activeLinks = linkRepository.findLinksFilteredByDelayTime(lastCheckId, batchSize, delayTime);

            if (activeLinks.isEmpty()) {
                break;
            }
            processor.runProcessLinks(activeLinks);

            //использовал @Transactional для решения проблемы N+1 при merge Hibernate, но кажется что
            //это не оптимальное решение
            linkRepository.updateLastCheckForLink(activeLinks);
            lastCheckId = activeLinks.getLast().getId();
        }
    }
}

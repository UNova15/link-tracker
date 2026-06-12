package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.messagesender.BotService;
import backend.academy.linktracker.scrapper.properties.ScrapperProperties;
import backend.academy.linktracker.scrapper.service.NotificationService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {
    private final BotService sender;
    private final NotificationService service;
    private final ScrapperProperties properties;

    @Scheduled(fixedDelayString = "${app.scrapper-settings.check-outbox-interval}")
    public void processNotification() {
        long lastCheckId = 0;

        while (true) {
            List<NotificationRecord> notificationRecords = service.capturingRecord(lastCheckId, properties.batchSize());

            if (notificationRecords.isEmpty()) {
                break;
            }
            List<Long> updatedIds = sender.send(notificationRecords);

            // освобождение захваченных записей
            service.releaseNotifications(notificationRecords, updatedIds);

            lastCheckId = notificationRecords.getLast().id();
        }
    }
}

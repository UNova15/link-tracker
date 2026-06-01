package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.properties.ScrapperProperties;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

// будет работать некорректно при использовании нескольких потоков/инстансов из за race condition db
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {
    private final MessageSender sender;
    private final NotificationRepository notificationRepository;
    private final ScrapperProperties properties;

    @Scheduled(fixedDelayString = "${app.scrapper-settings.check-outbox-interval}")
    public void processNotification() {
        long lastCheckId = 0;

        while (true) {
            List<NotificationRecord> notificationRecords =
                    notificationRepository.findBatchById(lastCheckId, properties.batchSize());

            if (notificationRecords.isEmpty()) {
                break;
            }

            List<Long> updatedIds = sender.send(notificationRecords);

            lastCheckId = notificationRecords.getLast().id();

            notificationRepository.delete(updatedIds);
        }
    }
}

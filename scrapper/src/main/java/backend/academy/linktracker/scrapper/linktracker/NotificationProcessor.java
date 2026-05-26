package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

// будет работать некорректно при использовании нескольких потоков/инстансов из за race condition db
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {
    private final MessageSender sender;
    private final NotificationRepository notificationRepository;

    @Value("${app.batch-size}")
    private long linksLimit;

    @Scheduled(fixedDelayString = "${app.check-outbox-interval}")
    public void processNotification() {
        long lastCheckId = 0;

        while (true) {
            List<NotificationRecord> notificationRecords =
                    notificationRepository.findBatchById(lastCheckId, linksLimit);

            if (notificationRecords.isEmpty()) {
                break;
            }

            List<Long> updatedIds = sender.send(notificationRecords);

            lastCheckId = notificationRecords.getLast().id();

            notificationRepository.delete(updatedIds);
        }
    }
}

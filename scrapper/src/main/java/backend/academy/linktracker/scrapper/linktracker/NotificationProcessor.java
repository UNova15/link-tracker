package backend.academy.linktracker.scrapper.linktracker;

import backend.academy.linktracker.scrapper.dto.linkdto.NotificationRecord;
import backend.academy.linktracker.scrapper.exception.TelegramBotException;
import backend.academy.linktracker.scrapper.messagesender.MessageSender;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProcessor {
    private final MessageSender sender;
    private final NotificationRepository notificationRepository;

    @Value("${app.batch-size}")
    private long linksLimit;

    @Scheduled(fixedDelayString = "${app.check_outbox-interval}")
    public void processNotification() {
        long lastCheckId = 0;

        while (true) {
            List<NotificationRecord> notifications = notificationRepository.findBatchById(lastCheckId, linksLimit);

            if (notifications.isEmpty()) {
                break;
            }
            List<Long> updatedIds = sendNotifications(notifications);

            lastCheckId = notifications.getLast().id();

            notificationRepository.delete(updatedIds);
        }
    }

    private List<Long> sendNotifications(List<NotificationRecord> updates) {
        List<Long> updated = new ArrayList<>();

        for (var update : updates) {
            try {
                sender.send(update.notification());
                updated.add(update.id());
            } catch (TelegramBotException exception) {
                log.error(
                        "Ошибка в уведомлении пользователей об изменениях по ссылке: {}. {}",
                        update.notification().getUrl(),
                        exception.getApiErrorResponse().description());
            }
        }
        return updated;
    }
}

package backend.academy.linktracker.ai.mapper;

import backend.academy.linktracker.ai.domain.Notification;
import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import backend.academy.linktracker.avro.RawLinkUpdate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDto toNotificationDto(RawLinkUpdate event) {
        return new NotificationDto(
                event.getIdempotenceKey(),
                event.getLinkId(),
                event.getUrl(),
                event.getAuthor(),
                event.getDescription(),
                event.getTgChatIds());
    }

    public ProcessedLinkUpdate toProcessedLinkUpdate(
            List<Notification> notifications, UUID idempotencyKey, String text) {

        long chatId = notifications.getFirst().tgChatId();
        List<Long> linksIds = notifications.stream().map(Notification::linkId).toList();
        return new ProcessedLinkUpdate(idempotencyKey, linksIds, text, chatId);
    }

    public List<Notification> toNotificationList(NotificationDto notificationDto) {
        return notificationDto.tgChatIds().stream()
                .map(chatId -> new Notification(
                        notificationDto.idempotenceKey(),
                        notificationDto.linkId(),
                        notificationDto.url(),
                        notificationDto.author(),
                        chatId,
                        notificationDto.description(),
                        null))
                .toList();
    }
}

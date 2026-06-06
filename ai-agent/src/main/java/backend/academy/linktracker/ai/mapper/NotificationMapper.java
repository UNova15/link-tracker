package backend.academy.linktracker.ai.mapper;

import backend.academy.linktracker.ai.dto.NotificationDto;
import backend.academy.linktracker.avro.ProcessedLinkUpdate;
import backend.academy.linktracker.avro.RawLinkUpdate;
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

    public ProcessedLinkUpdate toProcessedLinkUpdate(NotificationDto notification, UUID idempotencyKey, String text) {
        return new ProcessedLinkUpdate(idempotencyKey, notification.link_id(), text, notification.tgChatIds());
    }
}

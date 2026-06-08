package backend.academy.linktracker.ai.mapper;

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
            List<NotificationDto> notifications, UUID idempotencyKey, String text) {

        long chatId = notifications.getFirst().tgChatIds().getFirst();
        List<Long> linksIds =
                notifications.stream().map(NotificationDto::linkId).toList();
        return new ProcessedLinkUpdate(idempotencyKey, linksIds, text, chatId);
    }
}

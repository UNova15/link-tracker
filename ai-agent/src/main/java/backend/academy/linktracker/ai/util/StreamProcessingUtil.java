package backend.academy.linktracker.ai.util;

import backend.academy.linktracker.ai.dto.NotificationDto;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class StreamProcessingUtil {

    public List<NotificationDto> split(NotificationDto notificationDto) {
        if (notificationDto == null || notificationDto.tgChatIds().isEmpty()) {
            return List.of();
        }

        return notificationDto.tgChatIds().stream()
                .map(value -> new NotificationDto(
                        notificationDto.idempotenceKey(),
                        notificationDto.linkId(),
                        notificationDto.url(),
                        notificationDto.author(),
                        notificationDto.description(),
                        List.of(value)))
                .toList();
    }
}

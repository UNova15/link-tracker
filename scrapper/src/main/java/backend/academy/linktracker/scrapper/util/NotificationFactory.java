package backend.academy.linktracker.scrapper.util;

import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class NotificationFactory {

    public List<Notification> createNotifications(
            ProcessingResult result, long linkId, String url, List<Long> chatIds) {

        return result.updates().stream()
                .map(update -> {
                    // генерация детерминированного ключа идемпотентности для гарантии exactly once (для идемпотентности
                    // на стороне scrapper)
                    String uniquePayload = linkId + "_" + update.description();
                    UUID deterministicKey = UUID.nameUUIDFromBytes(uniquePayload.getBytes(StandardCharsets.UTF_8));

                    return Notification.createNew(
                            deterministicKey, linkId, url, update.author(), update.description(), chatIds);
                })
                .toList();
    }
}

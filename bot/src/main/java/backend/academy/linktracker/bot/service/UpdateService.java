package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.client.TelegramService;
import backend.academy.linktracker.bot.dto.NotificationDto;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class UpdateService {
    TelegramService telegram;
    Cache<UUID, Boolean> idempotencyCache;

    public void sendUpdateMessage(@Valid NotificationDto notification) {
        // обработка повторного сообщения
        if (idempotencyCache.getIfPresent(notification.idempotencyKey()) != null) {
            log.info("Дубликат сообщения :{} ключ: {}", notification.description(), notification.idempotencyKey());
            return;
        }
        idempotencyCache.put(notification.idempotencyKey(), Boolean.TRUE);

        for (long id : notification.tgChatIds()) {
            try {
                telegram.sendNotification(id, notification.description());
            } catch (Exception exception) {
                log.error("Ошибка отправки уведомления: {}, пользователь: {} ", notification.description(), id);
            }
        }
    }
}

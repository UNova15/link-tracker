package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.client.BotClient;
import backend.academy.linktracker.bot.dto.NotificationDto;
import backend.academy.linktracker.bot.properties.CacheProperties;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
@Slf4j
public class UpdateService {
    private final BotClient telegram;
    private final RedisTemplate<UUID, Boolean> valkey;
    private final CacheProperties properties;

    public void sendUpdateMessage(@Valid NotificationDto notification) {
        // обработка повторного сообщения
        if (!valkey.opsForValue()
                .setIfAbsent(
                        notification.idempotencyKey(),
                        true,
                        properties.idempotencyKey().ttl())) {
            log.info("Дубликат сообщения :{} ключ: {}", notification.description(), notification.idempotencyKey());
            return;
        }

        try {
            telegram.sendMessage(notification.tgChatId(), notification.description());
        } catch (Exception exception) {
            // в случае ошибки ключ удаляется чтобы не препятствовать ретраям
            valkey.delete(notification.idempotencyKey());
            log.error(
                    "Ошибка отправки уведомления: {}, пользователь: {}. Ошибка: {}",
                    notification.description(),
                    notification.tgChatId(),
                    exception.getMessage());
            throw exception;
        }
    }
}

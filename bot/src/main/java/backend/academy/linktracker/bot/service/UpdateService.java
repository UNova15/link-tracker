package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.dto.NotificationDto;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.validation.Valid;
import java.util.Set;
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
    private final TelegramBot bot;
    private final Set<UUID> keyStorage;

    public void sendUpdateMessage(@Valid NotificationDto notification) {
        // обработка повторного сообщения
        if (!keyStorage.add(notification.idempotencyKey())) {
            log.info("Дубликат сообщения :{} ключ: {}", notification.url(), notification.idempotencyKey());
            return;
        }

        for (long id : notification.tgChatIds()) {
            SendMessage message = new SendMessage(id, notification.description() + ": " + notification.url());
            bot.execute(message);
        }
    }
}

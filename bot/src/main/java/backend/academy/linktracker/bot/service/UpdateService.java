package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.domain.Notification;
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

    public void sendUpdateMessage(@Valid Notification notification) {
        // обработка повторного сообщения
        if (!keyStorage.add(notification.getIdempotencyKey())) {
            log.info("Дубликат сообщения :{} ключ: {}", notification.getUrl(), notification.getIdempotencyKey());
            return;
        }

        for (long id : notification.getTgChatIds()) {
            SendMessage message = new SendMessage(id, notification.getDescription() + ": " + notification.getUrl());
            bot.execute(message);
        }
    }
}

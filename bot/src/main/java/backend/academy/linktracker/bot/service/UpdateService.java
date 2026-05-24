package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@AllArgsConstructor
public class UpdateService {
    private final TelegramBot bot;

    public void sendUpdateMessage(@Valid LinkUpdate linkUpdate) {
        for (long id : linkUpdate.tgChatIds()) {
            SendMessage message = new SendMessage(id, linkUpdate.description() + ": " + linkUpdate.url());
            bot.execute(message);
        }
    }
}

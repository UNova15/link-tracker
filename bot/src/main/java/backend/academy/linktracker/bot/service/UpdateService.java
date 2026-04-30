package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.dto.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UpdateService {
    private final TelegramBot bot;

    public void sendUpdateMessage(LinkUpdate linkUpdate) {
        for (long id : linkUpdate.tgChatIds()) {
            SendMessage message = new SendMessage(id, linkUpdate.description() + ": " + linkUpdate.url());
            bot.execute(message);
        }
    }
}

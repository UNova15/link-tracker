package backend.academy.linktracker.bot.service;

import backend.academy.linktracker.bot.model.LinkUpdate;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UpdateService {
    private final TelegramBot bot;

    @Autowired
    public UpdateService(TelegramBot bot) {
        this.bot = bot;
    }

    public void sendUpdateMessage(LinkUpdate linkUpdate) {
        for (long id : linkUpdate.tgChatIds()) {
            SendMessage message = new SendMessage(id, linkUpdate.description() + ": " + linkUpdate.url());
            bot.execute(message);
        }
    }
}

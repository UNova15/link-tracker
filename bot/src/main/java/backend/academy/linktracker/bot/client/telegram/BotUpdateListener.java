package backend.academy.linktracker.bot.client.telegram;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BotUpdateListener implements UpdatesListener {

    private final TelegramBot bot;
    private final StateProcessor processor;

    @Autowired
    public BotUpdateListener(TelegramBot bot,StateProcessor processor) {
        this.bot = bot;
        this.processor = processor;
    }

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null) {
                long id = update.message().chat().id();
                String message = update.message().text();

                String response = processor.process(id,message);

                bot.execute(new SendMessage(id,response));
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}

package backend.academy.linktracker.bot.telegramservice;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BotUpdateListener implements UpdatesListener {
    private final TelegramBot bot;
    private final StateProcessor processor;

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {
            if (update.message() != null && update.message().text() != null) {
                long id = update.message().chat().id();
                String message = update.message().text();

                String response = processor.process(id, message);

                bot.execute(new SendMessage(id, response));
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}

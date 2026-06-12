package backend.academy.linktracker.bot.telegramservice;

import backend.academy.linktracker.bot.client.BotClient;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BotUpdateListener implements UpdatesListener {
    private final BotClient telegram;
    private final StateProcessor processor;

    @Override
    public int process(List<Update> updates) {
        for (Update update : updates) {

            if (update.message() == null
                    || update.message().text() == null
                    || update.message().text().isBlank()) continue;

            long id = update.message().chat().id();
            String message = update.message().text();
            String response = processor.process(id, message);

            try {
                telegram.sendMessage(id, response);
            } catch (Exception exception) {
                log.error("Ошибка при отправки ответа: {} пользователь: {} ", response, id);
            }
        }
        return CONFIRMED_UPDATES_ALL;
    }
}

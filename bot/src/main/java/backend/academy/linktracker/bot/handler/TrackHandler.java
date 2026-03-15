package backend.academy.linktracker.bot.handler;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class TrackHandler extends CommandHandler {

    public TrackHandler() {
        super(new BotCommand("/track", "Отслеживание ссылки"));
    }

    @Override
    public SendMessage handle(Update update) {
        long chatId = update.message().chat().id();
        return new SendMessage(chatId,"Введите ссылку для отслеживания");
    }
}

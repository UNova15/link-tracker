package backend.academy.linktracker.bot.service;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class UnknownCommandService {

    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        return new SendMessage(id,"Неизвестная команда");
    }
}

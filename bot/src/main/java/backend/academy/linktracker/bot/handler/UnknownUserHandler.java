package backend.academy.linktracker.bot.handler;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component
public class UnknownUserHandler implements Handler {

    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        return new SendMessage(id, "Неизвестная команда. Чтобы начать диалог выполните команду /start");
    }

}

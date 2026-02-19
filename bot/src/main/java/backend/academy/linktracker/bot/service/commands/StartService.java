package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class StartService extends CommandService {
    public StartService(){
        super("/start");
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        return new SendMessage(id, "Привет,выбери команду ниже");
    }
}

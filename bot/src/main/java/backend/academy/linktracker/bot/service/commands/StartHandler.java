package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

@Service
public class StartHandler extends CommandHandler {
    public StartHandler() {
        super(new BotCommand("/start", "Запуск бота"));
    }

    @Override
    public SendMessage handle(Update update) {
        long id = update.message().chat().id();
        return new SendMessage(id, "Добро пожаловать! Используйте /help, чтобы посмотреть доступные команды.");
    }
}

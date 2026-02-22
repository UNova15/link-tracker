package backend.academy.linktracker.bot.service.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Handler {
    SendMessage handle(Update update);
}

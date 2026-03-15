package backend.academy.linktracker.bot.handler.clientstatehandlers;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface State {
    SendMessage process(Update update);
}

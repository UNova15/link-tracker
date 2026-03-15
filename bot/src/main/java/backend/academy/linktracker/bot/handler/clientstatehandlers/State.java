package backend.academy.linktracker.bot.handler.clientstatehandlers;

import backend.academy.linktracker.bot.model.BotState;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.Getter;

@Getter
public abstract class State {
    private final BotState state;

    public State(BotState state) {
        this.state = state;
    }

    public abstract SendMessage process(Update update);
}

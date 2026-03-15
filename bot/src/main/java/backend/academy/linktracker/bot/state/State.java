package backend.academy.linktracker.bot.state;


import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;

public interface State {
    String process(UserMessage message, SessionData session);
}

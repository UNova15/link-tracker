package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;

public interface Handler {
    String handle(UserMessage message, SessionData session);
}

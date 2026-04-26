package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;

public interface Handler {
    String handle(UserMessage message, SessionData session);
}

package backend.academy.linktracker.bot.model;

public enum BotState {
    NEW,
    AWAIT_COMMAND,
    AWAIT_LINK,
    AWAIT_TAGS;
}

package backend.academy.linktracker.bot.handler.command;

import backend.academy.linktracker.bot.handler.StateChanger;
import backend.academy.linktracker.bot.model.Command;
import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.state.State;

public abstract class CommandHandler extends StateChanger {
    private final Command command;

    public CommandHandler(Command command, State newState) {
        super(newState);
        this.command = command;
    }

    public String getName() {
        return command.name();
    }

    public String getDescription() {
        return command.description();
    }

    @Override
    protected void changeState(SessionData session) {
        session.setState(newState);
        session.setLink(null);
        session.setTags(null);
    }
}

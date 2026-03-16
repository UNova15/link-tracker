package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.state.State;

public abstract class StateChanger implements Handler {
    protected final State newState;

    public StateChanger(State state) {
        this.newState = state;
    }

    protected void changeState(SessionData session) {
        session.setState(newState);
    }
}

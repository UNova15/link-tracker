package backend.academy.linktracker.bot.handler;

import backend.academy.linktracker.bot.client.telegram.SessionData;
import backend.academy.linktracker.bot.state.State;

public abstract class StateChanger implements Handler {
    private final State newState;

    public StateChanger(State state){
        this.newState = state;
    }

    public void changeState(SessionData session){
        session.setState(newState);
    }
}

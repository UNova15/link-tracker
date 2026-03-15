package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.handler.clientstatehandlers.State;
import backend.academy.linktracker.bot.model.BotState;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateDispatcher {
    private final Map<BotState, State> stateHandlers;

    public StateDispatcher(List<State> states) {
        this.stateHandlers = states.stream().collect(Collectors.toMap(State::getState, handler -> handler));
    }

    public State getStateHandler(SessionData sessionData){
        BotState state = sessionData.getState();
        return stateHandlers.get(state);
    }
}

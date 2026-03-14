package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.client.clientstatehandlers.StateHandler;
import backend.academy.linktracker.bot.model.BotState;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class StateDispatcher {
    private final Map<BotState, StateHandler> stateHandlers;

    public StateDispatcher(List<StateHandler> stateHandlers) {
        this.stateHandlers = stateHandlers.stream().collect(Collectors.toMap(StateHandler::getState, handler -> handler));
    }

    public StateHandler getStateHandler(SessionData sessionData){
        BotState state = sessionData.getState();
        return stateHandlers.get(state);
    }
}

package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.model.UserMessage;
import backend.academy.linktracker.bot.state.NewState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateProcessor {
    private final SessionStorage storage;
    private final NewState newState;

    @Autowired
    public StateProcessor(SessionStorage storage, NewState newState) {
        this.storage = storage;
        this.newState = newState;
    }

    public String process(long id, String text) {
        SessionData session = storage.findSession(id).orElseGet(() -> storage.createSession(id, newState));

        UserMessage message = new UserMessage(id, text);
        return session.getState().process(message, session);
    }
}

package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.domain.SessionData;
import backend.academy.linktracker.bot.domain.UserMessage;
import backend.academy.linktracker.bot.state.NewState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StateProcessor {
    private final SessionStorage storage;
    private final NewState newState;

    public String process(long id, String text) {
        SessionData session = storage.findSession(id).orElseGet(() -> storage.createSession(id, newState));

        UserMessage message = new UserMessage(id, text);
        return session.getState().process(message, session);
    }
}

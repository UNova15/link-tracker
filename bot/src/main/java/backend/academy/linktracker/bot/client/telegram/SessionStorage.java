package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.model.SessionData;
import backend.academy.linktracker.bot.state.State;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class SessionStorage {
    private final Map<Long, SessionData> storage = new ConcurrentHashMap<>();

    public SessionData createSession(long chatId, State state) {
        SessionData session = new SessionData(state);
        storage.put(chatId, session);
        return session;
    }

    public Optional<SessionData> findSession(long chatId) {
        return Optional.ofNullable(storage.get(chatId));
    }
}

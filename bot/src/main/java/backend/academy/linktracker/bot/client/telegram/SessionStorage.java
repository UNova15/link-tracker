package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.state.State;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
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

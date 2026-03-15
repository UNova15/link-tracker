package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.state.State;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionStorage {
    private final Map<Long, SessionData> storage = new ConcurrentHashMap<>();

    public SessionData createSession(long chatId, State state) {
        SessionData session = new SessionData(state);
        storage.put(chatId, session);
        return session;
    }

    public SessionData findSession(long chatId) {
        return storage.get(chatId);
    }

    public void updateSession(long chatId, State state) {
        storage.get(chatId).setState(state);
    }
}

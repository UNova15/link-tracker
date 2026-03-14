package backend.academy.linktracker.bot.client.telegram;

import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionStorage {
    private final Map<Long, SessionData> storage = new ConcurrentHashMap<>();

    public void saveSession(long chatId) {
        storage.put(chatId, new SessionData());
    }

    public SessionData findSession(long chatId) {
        return storage.computeIfAbsent(chatId,id -> new SessionData());
    }

    public void updateLink() {

    }
}

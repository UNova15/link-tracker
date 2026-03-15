package backend.academy.linktracker.bot.client.telegram;

import backend.academy.linktracker.bot.model.BotState;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class SessionStorage {
    private final Map<Long, SessionData> storage = new ConcurrentHashMap<>();

    public void saveSession(long chatId, BotState state) {
        storage.put(chatId, new SessionData(state));
    }

    public SessionData findSession(long chatId) {
        return storage.get(chatId);
    }

    public void updateLink() {

    }
}

package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.model.Chat;
import org.springframework.stereotype.Repository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ChatRepository {
    private final Map<Long, Chat> storage = new HashMap<>();

    public void saveChat(long chatId) {
        storage.put(chatId, new Chat(chatId, Instant.now()));
    }

    public void deleteChat(long chatId) {
        storage.remove(chatId);
    }

    public boolean exists(long chatId) {
        return storage.containsKey(chatId);
    }
}

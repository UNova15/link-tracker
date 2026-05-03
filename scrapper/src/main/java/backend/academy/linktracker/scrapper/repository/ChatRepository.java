package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Chat;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;

@Repository
public class ChatRepository {
    private final Map<Long, Chat> repository = new HashMap<>();

    public void saveChat(long chatId) {
        repository.put(chatId, new Chat(chatId, Instant.now()));
    }

    public void deleteChat(long chatId) {
        repository.remove(chatId);
    }

    public boolean exists(long chatId) {
        return repository.containsKey(chatId);
    }
}

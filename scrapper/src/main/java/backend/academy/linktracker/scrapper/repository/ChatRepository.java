package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.Chat;

public interface ChatRepository {
    void save(Chat chat);

    void deleteById(long chatId);

    boolean existById(long chatId);
}

package backend.academy.linktracker.scrapper.repository.sql;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.sql.dao.ChatDao;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
@AllArgsConstructor
public class SqlChatRepository implements ChatRepository {
    private final ChatDao chatDao;

    @Override
    public void save(Chat chat) {
        chatDao.save(chat);
    }

    @Override
    public void deleteById(long chatId) {
        chatDao.deleteById(chatId);
    }

    @Override
    public boolean existById(long chatId) {
        return chatDao.existById(chatId);
    }
}

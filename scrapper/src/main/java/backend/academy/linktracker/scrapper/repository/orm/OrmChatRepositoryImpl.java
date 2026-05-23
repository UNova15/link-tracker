package backend.academy.linktracker.scrapper.repository.orm;

import backend.academy.linktracker.scrapper.domain.Chat;
import backend.academy.linktracker.scrapper.repository.ChatRepository;
import backend.academy.linktracker.scrapper.repository.orm.entity.ChatEntity;
import backend.academy.linktracker.scrapper.repository.orm.jparepository.ChatJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app",name = "db-provider",havingValue = "orm")
@AllArgsConstructor
public class OrmChatRepositoryImpl implements ChatRepository {
    private final ChatJpaRepository chatRepository;

    @Override
    public void save(Chat chat) {
        ChatEntity newChat = ChatEntity.fromChat(chat);
        chatRepository.save(newChat);
    }

    @Override
    public void deleteById(long chatId) {
        chatRepository.deleteById(chatId);
    }

    @Override
    public boolean existById(long chatId) {
        return chatRepository.existsById(chatId);
    }
}

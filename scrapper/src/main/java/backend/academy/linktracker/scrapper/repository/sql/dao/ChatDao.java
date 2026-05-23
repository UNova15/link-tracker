package backend.academy.linktracker.scrapper.repository.sql.dao;

import backend.academy.linktracker.scrapper.domain.Chat;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app",name = "db-provider",havingValue = "sql")
@AllArgsConstructor
public class ChatDao {
    private final JdbcClient jdbcClient;

    public void save(Chat chat) {
        jdbcClient
                .sql("INSERT INTO chats (id,created_at) VALUES (:id,:created_at)")
                .param("id", chat.getChatId())
                .param("created_at", chat.getCreatedAt())
                .update();
    }

    public void deleteById(long chatId) {
        jdbcClient.sql("DELETE FROM chats WHERE id=:id ").param("id", chatId).update();
    }

    public boolean existById(long chatId) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM chats WHERE id=:id)")
                .param("id", chatId)
                .query(Boolean.class)
                .single();
    }
}

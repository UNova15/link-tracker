package backend.academy.linktracker.scrapper.repository.sql.dao;

import backend.academy.linktracker.scrapper.domain.Subscription;
import backend.academy.linktracker.scrapper.repository.sql.mapper.SubscriptionQueryMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(prefix = "app", name = "db-provider", havingValue = "sql")
@AllArgsConstructor
public class SubscriptionDao {
    private final JdbcTemplate template;
    private final JdbcClient jdbcClient;
    private final SubscriptionQueryMapper mapper;

    public List<Long> findChatsIdByLinkId(long linkId) {
        return jdbcClient
                .sql("SELECT chat_id FROM subscriptions WHERE link_id=:linkId")
                .param("linkId", linkId)
                .query(Long.class)
                .list();
    }

    public boolean exists(long chatId, long linkId) {
        return jdbcClient
                .sql("SELECT EXISTS(SELECT 1 FROM subscriptions WHERE chat_id=:chatId AND link_id=:linkId)")
                .param("chatId", chatId)
                .param("linkId", linkId)
                .query(Boolean.class)
                .single();
    }

    public List<Subscription> findSubscriptionsByChatId(long chatId) {
        return jdbcClient.sql("""
                SELECT s.chat_id, s.link_id, t.tag
                FROM subscriptions AS s
                LEFT JOIN tags_subscriptions AS t
                ON s.chat_id=t.chat_id AND s.link_id=t.link_id
                WHERE s.chat_id=:chatId
                """).param("chatId", chatId).query(mapper);
    }

    public void saveSubscriptionRecord(long chatId, long linkId) {
        jdbcClient
                .sql("INSERT INTO subscriptions (chat_id,link_id) VALUES (:chatId,:linkId)")
                .param("chatId", chatId)
                .param("linkId", linkId)
                .update();
    }

    public void saveTagsForSubscription(Subscription subscription) {
        List<String> tags = subscription.getTags();
        template.batchUpdate(
                "INSERT INTO tags_subscriptions (chat_id,link_id,tag) VALUES (?,?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, subscription.getChatId());
                        ps.setLong(2, subscription.getLinkId());
                        ps.setString(3, tags.get(i));
                    }

                    @Override
                    public int getBatchSize() {
                        return tags.size();
                    }
                });
    }

    public void removeSubscription(long chatId, long linkId) {
        jdbcClient
                .sql("DELETE FROM subscriptions WHERE chat_id=:chatId AND link_id=:linkId")
                .param("chatId", chatId)
                .param("linkId", linkId)
                .update();
    }

    public Optional<Subscription> findUsersSubscription(long chatId, long linkId) {
        List<Subscription> subscriptions = jdbcClient
                .sql("""
                SELECT s.chat_id, s.link_id, t.tag
                FROM subscriptions s
                LEFT JOIN tags_subscriptions t
                ON s.chat_id=t.chat_id AND t.link_id=s.link_id
                WHERE s.chat_id=:chatId AND s.link_id=:linkId
                """)
                .param("chatId", chatId)
                .param("linkId", linkId)
                .query(mapper);

        return subscriptions == null ? Optional.empty() : Optional.of(subscriptions.getFirst());
    }
}

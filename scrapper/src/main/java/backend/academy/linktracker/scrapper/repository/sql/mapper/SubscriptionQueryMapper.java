package backend.academy.linktracker.scrapper.repository.sql.mapper;

import backend.academy.linktracker.scrapper.domain.Subscription;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "app.access-type", havingValue = "SQL")
public class SubscriptionQueryMapper implements ResultSetExtractor<List<Subscription>> {

    @Override
    public List<Subscription> extractData(ResultSet rs) throws SQLException {
        Map<SubscriptionKey, List<String>> tagsBySubscription = new HashMap<>();

        while (rs.next()) {
            long linkId = rs.getLong("link_id");
            long chatId = rs.getLong("chat_id");
            String tag = rs.getString("tag");
            SubscriptionKey key = new SubscriptionKey(chatId, linkId);

            List<String> tags = tagsBySubscription.computeIfAbsent(key, k -> new ArrayList<>());

            if (tag != null) {
                tags.add(tag);
            }
        }

        return tagsBySubscription.entrySet().stream()
                .map(entry -> Subscription.createNew(
                        entry.getKey().chatId(), entry.getKey().linkId(), entry.getValue()))
                .toList();
    }
}

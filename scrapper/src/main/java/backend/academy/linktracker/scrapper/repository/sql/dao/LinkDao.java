package backend.academy.linktracker.scrapper.repository.sql.dao;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.repository.sql.mapper.LinkRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

@Repository
@Profile("sql")
@AllArgsConstructor
public class LinkDao {
    private final JdbcTemplate template;
    private final JdbcClient jdbcClient;
    private final LinkRowMapper mapper;

    public List<Link> findLinksToCheckWithDelayTime(long lastCheckId, long linksLimit, Instant delayTime) {
        return jdbcClient
            .sql("""
                SELECT * FROM links
                WHERE id >:lastCheckId
                AND last_check <= :delayTime
                ORDER BY id
                LIMIT :linksLimit
                """)
            .param("lastCheckId", lastCheckId)
            .param("linksLimit", linksLimit)
            .param("delayTime", Timestamp.from(delayTime))
            .query(mapper)
            .list();
    }

    public Link save(Link link) {
        Timestamp lastUpdate = link.getLastUpdate() != null
            ? Timestamp.from(link.getLastUpdate())
            : null;

        return jdbcClient
            .sql("INSERT INTO links (type,url,last_check,last_update) VALUES (:type,:url,:lastCheck,:lastUpdate) RETURNING *")
            .param("type", link.getType().toString())
            .param("url", link.getUrl())
            .param("lastCheck", Timestamp.from(link.getLastCheck()))
            .param("lastUpdate", lastUpdate)
            .query(mapper)
            .single();
    }

    public List<Link> findAllByIdIn(List<Long> linkIds) {
        return jdbcClient
            .sql("SELECT * FROM links WHERE id IN (:linkIds)")
            .param("linkIds", linkIds)
            .query(mapper)
            .list();
    }

    public void updateLastCheckAndLastUpdate(List<Link> links) {
        template.batchUpdate("UPDATE links SET last_check = ?,last_update = ? WHERE id = ?", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Link link = links.get(i);

                ps.setTimestamp(1, Timestamp.from(link.getLastCheck()));
                ps.setTimestamp(2, Timestamp.from(link.getLastUpdate()));
                ps.setLong(3, link.getId());
            }

            @Override
            public int getBatchSize() {
                return links.size();
            }
        });
    }

    public void removeByUrl(String url) {
        jdbcClient.sql("DELETE FROM links WHERE url=:url").param("url", url).update();
    }

    public Optional<Link> findByUrl(String url) {
        return jdbcClient
            .sql("SELECT * FROM links WHERE url=:url")
            .param("url", url)
            .query(mapper)
            .optional();
    }
}

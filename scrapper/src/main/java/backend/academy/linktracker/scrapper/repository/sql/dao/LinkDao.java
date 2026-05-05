package backend.academy.linktracker.scrapper.repository.sql.dao;

import backend.academy.linktracker.scrapper.domain.Link;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
@ConditionalOnProperty(name = "app.access-type", havingValue = "SQL")
@AllArgsConstructor
public class LinkDao {
    private final JdbcClient jdbcClient;

    public List<Link> getAll() {
        return jdbcClient.sql("SELECT * FROM links")
            .query(Link.class)
            .list();
    }

    public Link save(Link link) {
        return jdbcClient.sql("INSERT INTO links (type,url,last_check) VALUES (:type,:url,:lastCheck) RETURNING id")
            .param("type", link.getType().toString())
            .param("url", link.getUrl())
            .param("lastCheck", link.getLastCheck())
            .query(Link.class)
            .single();
    }

    public List<Link> findAllByIdIn(List<Long> linkIds) {
        return jdbcClient.sql("SELECT * FROM links WHERE id IN (:linkIds)")
            .param("linkIds", linkIds)
            .query(Link.class)
            .list();
    }

    public void update(Link link) {
        jdbcClient.sql("UPDATE links SET type=:type,url=:url,last_check=:lastCheck WHERE id=:linkId")
            .param("type", link.getType())
            .param("url", link.getUrl())
            .param("lastCheck", link.getLastCheck())
            .param("linkId", link.getId())
            .update();
    }

    public void removeByUrl(String url) {
        jdbcClient.sql("DELETE FROM links WHERE url=:url")
            .param("url", url)
            .update();
    }

    public Optional<Link> findByUrl(String url) {
        return jdbcClient.sql("SELECT * FROM links WHERE url=:url")
            .param("url", url)
            .query(Link.class)
            .optional();
    }

}

package backend.academy.linktracker.scrapper.repository.sql.mapper;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class LinkRowMapper implements RowMapper<Link> {
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        Timestamp lastUpdateTimeStamp = rs.getTimestamp("last_update");
        Instant lastUpdate = lastUpdateTimeStamp !=null
            ? lastUpdateTimeStamp.toInstant()
            : null;

        return Link.restore(
                rs.getLong("id"),
                LinkType.valueOf(rs.getString("type")),
                rs.getString("url"),
                rs.getTimestamp("last_check").toInstant(),
                lastUpdate
        );
    }
}

package backend.academy.linktracker.scrapper.repository.sql.mapper;

import backend.academy.linktracker.scrapper.domain.Link;
import backend.academy.linktracker.scrapper.domain.LinkType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LinkRowMapper implements RowMapper<Link>{
    @Override
    public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Link.restore(
            rs.getLong("id"),
            LinkType.valueOf(rs.getString("type")),
            rs.getString("url"),
            rs.getTimestamp("last_check").toInstant()
        );
    }
}

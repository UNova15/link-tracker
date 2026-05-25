package backend.academy.linktracker.scrapper.repository.sql.mapper;

import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.linkdto.NotificationRecord;
import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class NotificationRowMapper implements RowMapper<NotificationRecord> {
    private final ObjectMapper objectMapper;

    @Override
    public NotificationRecord mapRow(ResultSet rs, int rowNum) throws SQLException {
        long id = rs.getLong(1);
        String jsonRow = rs.getString(2);

        Notification notification = objectMapper.readValue(jsonRow, Notification.class);
        return new NotificationRecord(id, notification);
    }
}

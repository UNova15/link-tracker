package backend.academy.linktracker.scrapper.repository.sql.dao;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.repository.sql.mapper.NotificationRowMapper;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@AllArgsConstructor
public class NotificationDao {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcClient jdbcClient;

    private final NotificationRowMapper rowMapper;
    private final ObjectMapper objectMapper;

    public void saveOutboxRecord(List<Notification> notification) {
        jdbcTemplate.batchUpdate(
                "INSERT INTO notifications (link_update_event,status) VALUES (?::pg_catalog.jsonb,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        String json = objectMapper.writeValueAsString(notification.get(i));
                        ps.setObject(1, json);
                        ps.setObject(2, DBRecordStatus.IDLE.toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return notification.size();
                    }
                });
    }

    public List<NotificationRecord> findBatchById(long startId, long limit) {
        return jdbcClient
                .sql("""
                    SELECT * FROM notifications
                    WHERE status = 'IDLE'
                    AND id>:startId
                    ORDER BY id ASC
                    LIMIT :notificationLimit
                    FOR UPDATE SKIP LOCKED
                    """)
                .param("startId", startId)
                .param("notificationLimit", limit)
                .query(rowMapper)
                .list();
    }

    public void remove(List<Long> ids) {
        jdbcClient
                .sql("DELETE FROM notifications WHERE id IN (:ids)")
                .param("ids", ids)
                .update();
    }

    public void updateStatus(DBRecordStatus status, List<Long> ids) {
        jdbcClient
                .sql("UPDATE notifications SET status =:status WHERE id IN (:ids)")
                .param("status", status.toString())
                .param("ids", ids)
                .update();
    }
}

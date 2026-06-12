package backend.academy.linktracker.scrapper.repository.sql;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import backend.academy.linktracker.scrapper.repository.sql.dao.NotificationDao;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class SqlNotificationRepository implements NotificationRepository {
    private final NotificationDao notificationDao;

    @Override
    public void save(List<Notification> notification) {
        notificationDao.saveOutboxRecord(notification);
    }

    @Override
    public List<NotificationRecord> findBatchById(long lastCheckId, long notificationLimit) {
        return notificationDao.findBatchById(lastCheckId, notificationLimit);
    }

    @Override
    public void delete(List<Long> notificationIds) {
        notificationDao.remove(notificationIds);
    }

    @Override
    public void updateStatus(List<Long> ids, DBRecordStatus status) {
        notificationDao.updateStatus(status, ids);
    }
}

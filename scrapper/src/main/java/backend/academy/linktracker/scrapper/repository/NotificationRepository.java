package backend.academy.linktracker.scrapper.repository;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import java.util.List;

public interface NotificationRepository {
    void save(List<Notification> notification);

    List<NotificationRecord> findBatchById(long lastCheckId, long notificationLimit);

    void delete(List<Long> notificationIds);

    void updateStatus(List<Long> ids, DBRecordStatus status);
}

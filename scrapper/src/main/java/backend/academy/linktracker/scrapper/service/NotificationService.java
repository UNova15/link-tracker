package backend.academy.linktracker.scrapper.service;

import backend.academy.linktracker.scrapper.domain.DBRecordStatus;
import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import backend.academy.linktracker.scrapper.repository.NotificationRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class NotificationService {
    private final NotificationRepository repository;

    @Transactional
    public List<NotificationRecord> capturingRecord(long lastCheckId, long batchSize) {
        List<NotificationRecord> records = repository.findBatchById(lastCheckId, batchSize);

        if (records.isEmpty()) {
            return List.of();
        }

        List<Long> ids = records.stream().map(NotificationRecord::id).toList();
        repository.updateStatus(ids, DBRecordStatus.PROCESSING);

        return records;
    }

    private void deleteRecords(List<Long> updatedIds) {
        if (updatedIds.isEmpty()) {
            return;
        }
        repository.delete(updatedIds);
    }

    @Transactional
    public void releaseNotifications(List<NotificationRecord> notificationRecords, List<Long> updatedIds) {

        List<Long> failedIds;
        if (updatedIds == null || updatedIds.isEmpty()) {
            failedIds = notificationRecords.stream().map(NotificationRecord::id).toList();
            repository.updateStatus(failedIds, DBRecordStatus.IDLE);

            return;
        }

        deleteRecords(updatedIds);

        if (updatedIds.size() == notificationRecords.size()) {
            return;
        }

        failedIds = notificationRecords.stream()
                .map(NotificationRecord::id)
                .filter(id -> !updatedIds.contains(id))
                .toList();

        repository.updateStatus(failedIds, DBRecordStatus.IDLE);
    }
}

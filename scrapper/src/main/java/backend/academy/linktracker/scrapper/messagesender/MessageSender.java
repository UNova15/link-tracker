package backend.academy.linktracker.scrapper.messagesender;

import backend.academy.linktracker.scrapper.dto.sender.NotificationRecord;
import java.util.List;

public interface MessageSender {

    List<Long> send(List<NotificationRecord> records);
}

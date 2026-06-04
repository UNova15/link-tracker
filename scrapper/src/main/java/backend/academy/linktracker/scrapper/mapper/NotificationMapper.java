package backend.academy.linktracker.scrapper.mapper;

import backend.academy.linktracker.avro.LinkUpdateEvent;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.dto.linkdto.Update;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.UUID;

@Component
public class NotificationMapper {

    public List<Update> fromStackOverflowContentToUpdate(List<StackOverflowContent> contents) {
        return contents.stream()
                .map(content -> new Update(content.user().name(), content.body()))
                .toList();
    }

    public List<Update> fromGitHubContentToUpdate(List<GitHubResponse> contents) {
        return contents.stream()
                .map(content -> new Update(content.user().login(), content.description()))
                .toList();
    }

    public LinkUpdateEvent toLinkUpdateEvent(Notification notificationRecord) {
        return new LinkUpdateEvent(
                notificationRecord.getIdempotencyKey(),
                notificationRecord.getLinkId(),
                notificationRecord.getUrl(),
                notificationRecord.getDescription(),
                notificationRecord.getTgChatIds());
    }

    public List<Notification> toNotification(
            ProcessingResult result, UUID key, long linkId, String url, List<Long> chatIds) {
        return result.updates().stream()
                .map(update -> Notification.createNew(key, linkId, url, update.author(), update.description(), chatIds))
                .toList();
    }
}

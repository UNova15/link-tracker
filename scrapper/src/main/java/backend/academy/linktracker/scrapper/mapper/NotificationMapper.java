package backend.academy.linktracker.scrapper.mapper;

import backend.academy.linktracker.avro.RawLinkUpdate;
import backend.academy.linktracker.scrapper.domain.Notification;
import backend.academy.linktracker.scrapper.dto.github.GitHubResponse;
import backend.academy.linktracker.scrapper.dto.linkdto.ProcessingResult;
import backend.academy.linktracker.scrapper.dto.linkdto.Update;
import backend.academy.linktracker.scrapper.dto.stackoverflow.StackOverflowContent;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Component;

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

    public RawLinkUpdate toLinkUpdateEvent(Notification notification) {
        return new RawLinkUpdate(
                notification.getIdempotencyKey(),
                notification.getLinkId(),
                notification.getUrl(),
                notification.getAuthor(),
                notification.getDescription(),
                notification.getTgChatIds());
    }

    public List<Notification> toNotification(
            ProcessingResult result, UUID key, long linkId, String url, List<Long> chatIds) {
        return result.updates().stream()
                .map(update -> Notification.createNew(key, linkId, url, update.author(), update.description(), chatIds))
                .toList();
    }
}
